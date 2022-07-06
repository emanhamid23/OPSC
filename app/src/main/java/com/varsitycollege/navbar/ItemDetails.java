package com.varsitycollege.navbar;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import models.Category;
import models.Data;

public class ItemDetails extends AppCompatActivity {

    //CONNECT TO FIREBASE - FIRESTORE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

    //GETTING ALL VALUES
    TextView showtxt, getcatname2;
    public String selectedItem;
    ProgressBar progressBar;
    TextView textView, getgoal;
    ListView itemdetailslistview;
    Button progressbutton, backbtn;
    int num;
    LottieAnimationView confetti;

    //ARRAY FOR DISPLAYING DATA
    ArrayList<Data> datas = new ArrayList<>();

    //START
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        //FIND VIEW BY ID
        itemdetailslistview = findViewById(R.id.itemdetailslistview);
        backbtn = findViewById(R.id.backbtn);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
        progressbutton = findViewById(R.id.progressbutton);

        //CONFETTI
        confetti = findViewById(R.id.confetti);
        confetti.setVisibility(View.INVISIBLE);

        //SETTING CATEGORY NAME
        getcatname2 = (TextView) findViewById(R.id.getcatname2);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key2");
        getcatname2.setText(str);

        //PROGRESS BAR
        //SETTING PROGRESS_BAR NUMBERS
        Intent i = getIntent();
        String st = i.getStringExtra("key");
        num = Integer.parseInt(st);
        progressBar.setProgress(num);

        //DISPLAYING DATA IN A LIST
        ArrayList<String> onlyData = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, onlyData);
        db.collection("data")
                .whereEqualTo("category", getcatname2.getText())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String name = String.valueOf(document.getData().get("item"));
                                String date = String.valueOf(document.getData().get("dateChoosen"));
                                String categoryname = String.valueOf(document.getData().get("category"));
                                String itemdescription = String.valueOf(document.getData().get("descriptionItem"));

                                Data dat = new Data(name, date, categoryname, itemdescription);
                                datas.add(dat);
                            }
                            for (Data x : datas)
                            {
                                onlyData.add("\n" + "Item Name:  " + x.getName() + "\nDate Selected:  " + x.getCategoryname() + "\nCategory:  " +
                                        "" + x.getDate() + "\nItem Description:  " + x.getItemdescription() + "\n");
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Log.w("==============> ", "Error getting documents.", task.getException());
                        }
                    }
                });

        //ADDINNG DATA TO THE LIST
        itemdetailslistview.setAdapter(adapter);

        //VIEWING ITEM PROGRESS BUTTON
        progressbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    //GENERATING A RANDOM STRING TO COUPON CODE:
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    //RANDOM STRING METHOD
    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    //SHOWING PROGRESS ON BAR - COUPON
    private void ShowProgress(){
        //SHOWING DATA ON PROGRESS BAR
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference deliveryRef = rootRef.collection("categories");
        Query nameQuery = deliveryRef.whereEqualTo("name", getcatname2.getText());
        nameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        //GETTING DATA FROM DB - GOAL
                        Long amount = (Long) document.get("goal");;

                        if(amount != null)
                        {
                            progressBar.setMax(amount.intValue());

                            //ITEMS - GOAL = REMAINING PROGRESS BAR
                            int value = amount.intValue() - num;
                            textView.setText(value + " remaining");

                            if (num == amount.intValue())
                            {
                                confetti.setVisibility(View.VISIBLE);
                                confetti.animate()
                                        .alpha(0)
                                        .setDuration(2000)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {

                                                //ELSE SHOW THIS METHOD
                                                textView.setText("Your Progress has been Completed!");
                                                Toast.makeText(ItemDetails.this, "Your Progress has been Completed", Toast.LENGTH_SHORT).show();
                                                //ALERT DIALOG
                                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ItemDetails.this);
                                                dlgAlert.setMessage("\n" + "You have received a new Coupon!" + "\n" + "Coupon code:" + "\n"+ getRandomString(7));
                                                dlgAlert.setTitle("Congratulations!");
                                                dlgAlert.setPositiveButton("OK", null);
                                                dlgAlert.setCancelable(true);
                                                dlgAlert.setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //dismiss the dialog
                                                            }
                                                        });
                                                dlgAlert.setNegativeButton("Claim Coupons", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //WHEN USER CLICKS ON CLAIM, THIS WEBSITE WILL OPEN
                                                        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.thecrowncollection.co.za/antique-shop/"));
                                                        startActivity(intent);
                                                    }
                                                });
                                                dlgAlert.create().show();
                                            }
                                        });
                            }
                        }
                        else
                        {
                            Log.d(TAG, "Got bad data for " + document.getId() + " => " + document.getData());
                        }
                    }
                }
            }
        });
    }

    //OPENS THE COUPON DETAILS SCREEN
    private void openWinDialog()
    {
       Intent i = new Intent(ItemDetails.this, Coupon.class);
       startActivity(i);
    }
}


