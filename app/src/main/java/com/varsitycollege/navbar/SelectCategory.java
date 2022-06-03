package com.varsitycollege.navbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import models.Category;

public class SelectCategory extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

    //self compoents
    ListView categoriesList;
    Button addCatBtn;
    ArrayList<Category> categories = new ArrayList<>();
    String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        categoriesList = findViewById(R.id.categoriesList);
        ArrayList<String> onlyCategories = new ArrayList<>();
        addCatBtn = (Button) findViewById(R.id.addCatBtn);

        //first thing, disable button
        addCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectCategory.this, Categories.class);
                startActivity(i);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, onlyCategories);
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = String.valueOf(document.getData().get("name"));
                                String goal = String.valueOf(document.getData().get("goal"));
                                Category category = new Category(name, goal);
                                categories.add(category);
                            }
                            for (Category x: categories) {
                                onlyCategories.add(x.getName());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("==============> ", "Error getting documents.", task.getException());
                        }
                    }
                });

        categoriesList.setAdapter(adapter);


        //select a category
        categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = onlyCategories.get(position);
                Intent i = new Intent(SelectCategory.this, AddItems.class);
                i.putExtra("cat_type", selection);
                startActivity(i);
            }
        });

    }
}


//    Map<String, Object> user = new HashMap<>();
//user.put("first", "Alan");
//        user.put("middle", "Mathison");
//        user.put("last", "Turing");
//        user.put("born", 1912);
//
//// Add a new document with a generated ID
//        db.collection("users")
//        .add(user)
//        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//@Override
//public void onSuccess(DocumentReference documentReference) {
//        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        Log.w(TAG, "Error adding document", e);
//        }
//        });