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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import models.Category;

public class AddItems extends AppCompatActivity {

    //IMAGE INFO
    private Uri filePath;
    final int ReqValue = 101;

    //GETTING ALL VALUES
    Button btnAdd, viewitemslist, btnback;
    EditText categoryName, itemName, description, dateChoosen, getgoals;
    DrawerLayout drawerLayout;
    TextView getcatname1;

    String browserIntent;
    //IMAGE
    private ImageView imageSelector;

    //CONNECT TO FIREBASE - FIRESTORE
    FirebaseFirestore FB_Firestore = FirebaseFirestore.getInstance();
    FirebaseStorage FB_Storage = FirebaseStorage.getInstance();
    StorageReference storageRef = FB_Storage.getReferenceFromUrl("gs://opsc-poe-dcc80.appspot.com");

    //START
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        //FIND VIEW BY ID
        drawerLayout = findViewById(R.id.drawer_layout);
        itemName = findViewById(R.id.itemname);
        description = findViewById(R.id.itemdesc);
        dateChoosen = findViewById(R.id.itemdate);
        btnAdd = findViewById(R.id.btnAddItem);
        viewitemslist = findViewById(R.id.btnviewitemlist);
        imageSelector = findViewById(R.id.imageSelector);


        //SETTING CATEGORY NAME
        String category = getIntent().getStringExtra("cat_type");
        categoryName = findViewById(R.id.category_name);
        categoryName.setText(category);
        categoryName.setFocusable(false);

        //SETTING CATEGORY NAME TO PAGE TITLE
        getcatname1 = findViewById(R.id.getcatname1);
        getcatname1.setText(category);
        getcatname1.setFocusable(false);

        //SHOWS LIST OF ITEMS
        viewitemslist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //SENDING VALUES FROM THIS SCREEN TO NEXT SCREEN
                String str = categoryName.getText().toString();
               // Integer str2 = Integer.parseInt(getgoals.getText().toString());
                Intent intent = new Intent(getApplicationContext(), Showitems.class);
                intent.putExtra("message_key", str); // CATEGORY NAME
              //  intent.putExtra("getting_goal", str2); // GOAL
                startActivity(intent);
            }
        });

        //IMAGE SELECTOR
        imageSelector.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), ReqValue);
            }
        });

        //ADD BUTTON
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //ERROR MESSAGES
                if(TextUtils.isEmpty(itemName.getText().toString()))
                {
                    itemName.setError("This field can't be empty");
                }
                else if(TextUtils.isEmpty(description.getText().toString()))
                {
                    description.setError("This field can't be empty");
                }
                else if(TextUtils.isEmpty(dateChoosen.getText().toString()))
                {
                    dateChoosen.setError("This field can't be empty");
                }
                else{
                    uploadImage();
                }
            }
        });
    }

    //ADDING CONTENT TO FIRE STORE
    private void addContent(Uri URL)
    {
        String category = categoryName.getText().toString();
        String item = itemName.getText().toString();
        String descriptionItem = description.getText().toString();
        String Date = dateChoosen.getText().toString();

        System.out.println("LINK IS => " + URL);

        Item entry = new Item(category, item, descriptionItem, Date, URL);

        FB_Firestore.collection("data")
                .add(entry)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        Toast.makeText(AddItems.this, "Item Added!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        //Log.w("=====================> ", "Error adding document", e);
                        Toast.makeText(AddItems.this, "Unsuccessful Entry.Try again.!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //IMAGE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == ReqValue && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            // Get the Uri of data
            filePath = data.getData();
            try
            {
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
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    //UPLOADING IMAGE
    private void uploadImage()
    {
        final Uri URL;
        if (filePath != null)
        {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    //IMAGE UPLOAD MESS:
                    Toast.makeText(AddItems.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            addContent(uri);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(AddItems.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
            {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }
}
