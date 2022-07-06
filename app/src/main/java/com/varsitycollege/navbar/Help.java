package com.varsitycollege.navbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Help extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        drawerLayout = findViewById(R.id.drawer_layout);
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
        //SEND TO LOG OUT
        redirectActivity(this, WelcomeScreen.class);
    }

    //REDIRECTING METHOD
    private static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }
}