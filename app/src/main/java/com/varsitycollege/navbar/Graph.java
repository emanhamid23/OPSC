package com.varsitycollege.navbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static android.content.ContentValues.TAG;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class Graph extends AppCompatActivity {

    //CONNECT TO FIREBASE - FIRE STORE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage FB_Storage = FirebaseStorage.getInstance();

    //GETTING ALL VALUES
    private PieChart pieChart;
    TextView getgoal;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //FIND VIEW BY ID
        pieChart = findViewById(R.id.pieChart);
        getgoal = (TextView) findViewById(R.id.getgoal);
        drawerLayout = findViewById(R.id.drawer_layout);

        //SETTING GOAL
        Intent in = getIntent();
        String s = in.getStringExtra("get_goal");
        getgoal.setText(s);

        setupPieChart();

        //GETTING ALL DATA FROM FIRE STORE TO DISPLAY IN GRAPH
        db.collection("categories").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful()){
                            ArrayList<PieEntry> entries = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d(TAG,document.getId()+ " => " + document.getData());

                                Map<String,Object> data = document.getData();
                                String cat =(String) data.get("name");
                                Long amount = (Long) data.get("goal");

                                if(cat != null && amount != null)
                                {
                                    entries.add(new PieEntry(amount.floatValue(), cat));
                                }else
                                {
                                    Log.d(TAG, "Got bad data for " + document.getId() + " => " + document.getData());
                                }
                            }
                            showPieChart(entries);
                        }else
                        {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                }
        );
    }

    //SHOWING PIE CHART
    void showPieChart(ArrayList<PieEntry> entries)
    {
        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS)
        {
            colors.add(color);
        }
        for (int color: ColorTemplate.VORDIPLOM_COLORS)
        {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, " categories");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    //SET UP PIE CHART
    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Items Per Category");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    //TO OPEN
    //OPEN NAV DRAWER
    public void Click_menu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //METHOD FOR OPENING NAV
        drawerLayout.openDrawer(GravityCompat.START);
    }

    //TO CLOSE
    //CLICK LOGO TO CLOSE NAVBAR
    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //CLOSE NAV METHOD
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //OPEN EACH ITEM FROM NAVBAR
    //CLICK HOME
    public void ClickHome(View view){
        //SEND TO HOME PAGE
        redirectActivity(this, WelcomeScreen.class);
    }

    public void ClickCategories(View view){
        //SEND TO CATEGORIES PAGE
        redirectActivity(this, SelectCategory.class);
    }

    public void Clickwishlist(View view){
        //SEND TO WISHLIST PAGE
        redirectActivity(this, Wishlist.class);
    }

    public void ClickGraph(View view){
        //SEND TO MARKETPLACE PAGE
        redirectActivity(this, Graph.class);
    }

    public void ClickCoupon(View view) {
        //SEND TO GRAPH PAGE
        redirectActivity(this, Coupon.class);
    }

    public void Clickusermanual(View view) {
        //SEND TO USERMANUAL PAGE
        redirectActivity(this, UserManual.class);
    }

    public void Clickhelp(View view) {
        //SEND TO HELP PAGE
        redirectActivity(this, Help.class);
    }

    public void Clicksettings(View view) {
        //SEND TO SETTINGS
        redirectActivity(this, Setting.class);
    }

    public void Clicklogout(View view) {
        //LOG OUT OF THE APP
        logout(this);
    }

    //METHOD OF LOGGING OUT
    public static void logout(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you want to log out ? ");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    //REDIRECTING METHOD
    private static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}