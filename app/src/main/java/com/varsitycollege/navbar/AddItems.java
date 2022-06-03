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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddItems extends AppCompatActivity {

    private Uri filePath;
    final int ReqValue = 101;

    Button btnAdd, itemlistbtn;
    EditText categoryName, itemName, description, dateChoosen;
    DrawerLayout drawerLayout;

    private ImageView imageSelector;

    FirebaseFirestore FB_Firestore = FirebaseFirestore.getInstance();
    FirebaseStorage FB_Storage = FirebaseStorage.getInstance();
    StorageReference storageRef = FB_Storage.getReferenceFromUrl("gs://opsc-poe-dcc80.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        String category = getIntent().getStringExtra("cat_type");

        drawerLayout = findViewById(R.id.drawer_layout);

        categoryName = findViewById(R.id.category_name);
        categoryName.setText(category);
        categoryName.setFocusable(false);
        itemName = findViewById(R.id.itemname);
        description = findViewById(R.id.itemdesc);
        dateChoosen = findViewById(R.id.itemdate);
        btnAdd = findViewById(R.id.btnAddItem);
        itemlistbtn = findViewById(R.id.viewitemlist);
        imageSelector = findViewById(R.id.imageSelector);

        //SHOWS LIST OF CATEGORIES
        itemlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItems.this, ItemsList.class);
                startActivity(intent);
            }
        });

        imageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), ReqValue);
            }
        });





        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                //startActivity(new Intent(AddItems.this, ItemsList.class));
            }
        });
    }

    private void addContent(Uri URL) {
        String category = categoryName.getText().toString();
        String item = itemName.getText().toString();
        String descriptionItem = description.getText().toString();
        String Date = dateChoosen.getText().toString();

        System.out.println("LINK IS => " + URL);

        Item entry = new Item(category, item, descriptionItem, Date, URL);

        FB_Firestore.collection("data")
                .add(entry)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddItems.this, "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("=====================> ", "Error adding document", e);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == ReqValue && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageSelector.setImageBitmap(bitmap);

                //now once image has been set, upload it to Firebase in the 'storeUserItems' method

            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        final Uri URL;
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(AddItems.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            addContent(uri);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(AddItems.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
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
