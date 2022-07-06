package com.varsitycollege.navbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class CategoryDetails extends AppCompatActivity {

    //CONNECT TO FIREBASE - FIRESTORE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

    //GETTING ALL VALUES
    DrawerLayout drawerLayout;
    ListView categoriesList;
    Button backbtn;

    //ARRAY LIST FOR DISPLAYING DATA IN LISTVIEW
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<String> onlyCategories = new ArrayList<>();

    String selectedCategory = "";

    //START
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        //FIND VIEW BY ID
        categoriesList = findViewById(R.id.categorydetailslistview);

        //SHOWING LIST OF CATEGORIES
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, onlyCategories);
        db.collection("categories")
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
                                String name = String.valueOf(document.getData().get("name"));
                                Integer goal = Integer.parseInt(String.valueOf(document.getData().get("goal")));
                                Category category = new Category(name, goal);
                                categories.add(category);
                            }
                            for (Category x: categories)
                            {
                                onlyCategories.add("\n"+"Name:   "+x.getName() +"\nGoal:    "+x.getGoal() + "\n");

                                adapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Log.w("==============> ", "Error getting documents.", task.getException());
                        }
                    }
                });

        //ADDING THE DATA TO LISTVIEW
        categoriesList.setAdapter(adapter);
    }
}