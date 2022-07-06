package com.varsitycollege.navbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeScreen extends AppCompatActivity {
DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void Click_menu(View view){

        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {

        drawerLayout.openDrawer(GravityCompat.START);
    }

    //opening ites from navbar
    public void ClickHome(View view){

        redirectActivity(this, WelcomeScreen.class);
    }

    public void ClickCategories(View view){

        redirectActivity(this, SelectCategory.class);
    }

    public void Clickwishlist(View view){

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

        redirectActivity(this, UserManual.class);
    }

    public void Clickhelp(View view) {
        redirectActivity(this, Help.class);

    }

    public void Clicksettings(View view) {

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

    private static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}