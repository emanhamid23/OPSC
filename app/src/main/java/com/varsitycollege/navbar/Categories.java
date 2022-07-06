package com.varsitycollege.navbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import models.Category;

public class Categories extends AppCompatActivity {

    //GETTING ALL VALUES
    DrawerLayout drawerLayout;
    EditText catname, catgoal;
    Button addcat, viewlist;

    //CONNECT TO FIREBASE - FIRESTORE
    DatabaseReference databaseUsers;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //START
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        //FIND VIEW BY ID
        drawerLayout = findViewById(R.id.drawer_layout);
        viewlist = findViewById(R.id.viewcatlist);
        addcat = findViewById(R.id.addcat);
        catname = findViewById(R.id.catname);
        catgoal = findViewById(R.id.catgoal);

        //FIREBASE CONN
        databaseUsers = FirebaseDatabase.getInstance().getReference();

        //ADDS DATA TO FB
        addcat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(TextUtils.isEmpty(catname.getText().toString()))
                {
                    catname.setError("This field can't be empty");
                }
                else if(TextUtils.isEmpty(catgoal.getText().toString()))
                {
                    catgoal.setError("This field can't be empty");
                }
                else{
                    //METHOD TO INSERT DATA
                    InsertData();
                }
            }
        });

        //SHOWS A LIST OF CATEGORIES
        viewlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Categories.this, SelectCategory.class);
                startActivity(intent);
            }
        });
    }

    //INSERT DATA TO FB METHOD
    private void InsertData()
    {
        String category = catname.getText().toString();
        Integer goal = Integer.parseInt(catgoal.getText().toString());

        //USER CLASS
        Category cat = new Category(category, goal);
        db.collection("categories")
                .add(cat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        Toast.makeText(Categories.this, "New Category Added!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Categories.this, SelectCategory.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.w("=============> ", "Error adding document", e);
                    }
                });
    }
}