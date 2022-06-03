package com.varsitycollege.navbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddItems extends AppCompatActivity {

    Button btnAdd, itemlistbtn;
    EditText categoryName, itemName, description, dateChoosen;
    private static final int Gallery_Code=1;
    Uri imageUrl=null;
    ProgressDialog progressDialog;
    DrawerLayout drawerLayout;

    private ImageView upload;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("categories");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        drawerLayout = findViewById(R.id.drawer_layout);

        categoryName = findViewById(R.id.category_name);
        itemName = findViewById(R.id.itemname);
        description = findViewById(R.id.itemdesc);
        dateChoosen = findViewById(R.id.itemdate);
        btnAdd = findViewById(R.id.btnAddItem);
        itemlistbtn = findViewById(R.id.viewitemlist);

        upload = findViewById(R.id.image);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //SHOWS LIST OF CATEGORIES
        itemlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItems.this, ItemsList.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeUserItems();
                startActivity(new Intent(AddItems.this, ItemsList.class));
            }
        });
    }

                private void storeUserItems(){
                    String category = categoryName.getText().toString();
                    String item = itemName.getText().toString();
                    String descriptionItem = description.getText().toString();
                    String Date = dateChoosen.getText().toString();

                    String id = myRef.push().getKey();

                    Item items = new Item(category, item, descriptionItem, Date);

                    myRef.child("items").child(id).setValue(items).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddItems.this, "Item added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

    ///////////////////////////////////////////////////////////

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

    //OPEN EACH ITEM FRO NAVBAR
    //CLICK HOME
    public void ClickHome(View view){
        //SEND TO HOME PAGE
        redirectActivity(this, WelcomeScreen.class);
    }

    public void ClickCategories(View view){
        //SEND TO CATEGORIES PAGE
        redirectActivity(this, Categories.class);
    }

    public void Clickwishlist(View view){
        //SEND TO WISHLIST PAGE
        redirectActivity(this, categoryentry.class);
    }

    public void Clickmarketplace(View view){
        //SEND TO MARKETPLACE PAGE
        redirectActivity(this, Market_place.class);
    }

    public void Clickadditem(View view) {
        //SEND TO GRAPH PAGE
        redirectActivity(this, WelcomeScreen.class);
    }

    public void Clickthemes(View view) {
        //SEND TO THEMES PAGE
        redirectActivity(this, categoryentry.class);
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
