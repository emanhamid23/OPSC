package com.varsitycollege.navbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import java.util.Map;

import models.Category;
import models.Goal;

public class SelectCategory extends AppCompatActivity {

    //CONNECT TO FIREBASE - FIRESTORE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

    public static int message;

    //GETTING ALL VALUES
    ListView categoriesList, goalList;
    Button addCatBtn, backbtn, viewdetails;
    DrawerLayout drawerLayout;
    ProgressDialog progressDialog;

    //ARRAY TO DISPLAY DATA
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<String> onlyCategories = new ArrayList<>();

    String selectedCategory = "";

    //START
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        //SHOW PROGRESS BAR
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while getting list of categories. . .");
        progressDialog.setTitle("Category Lists");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //FIND VIEW BY ID
        goalList = findViewById(R.id.goalList);
        categoriesList = findViewById(R.id.categoriesList);
        backbtn = findViewById(R.id.backbtn);
        drawerLayout = findViewById(R.id.drawer_layout);
        addCatBtn = (Button) findViewById(R.id.addCatBtn);
        viewdetails = findViewById(R.id.viewdetails);


        //SHOW DETAILS OF ALL CATEGORIES
        viewdetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SelectCategory.this, CategoryDetails.class);
                startActivity(i);
            }
        });

        //ADD A CATEGORY BUTTON
        addCatBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(SelectCategory.this, Categories.class);
                startActivity(i);
            }
        });

        //DISPLAYING LIST OF CATEGORIES
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
                                onlyCategories.add(x.getName());
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Log.w("==============> ", "Error getting documents.", task.getException());
                        }
                    }
                });

        //ADDING DATA TO LIST
        categoriesList.setAdapter(adapter);

       // progressDialog.dismiss();
        //GETTING GOAL

        //ARRAY TO STORE DETAILS
        ArrayList<Category> goals = new ArrayList<>();
        ArrayList<Integer> onlyGoals = new ArrayList<>();

        ArrayAdapter<Integer> goaladapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, onlyGoals);
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String category = String.valueOf(document.getData().get("name"));
                                Integer goal = Integer.parseInt(String.valueOf(document.getData().get("goal")));
                                Category g = new Category(category, goal);
                                goals.add(g);
                            }
                            for (Category x: goals) {
                                onlyGoals.add(x.getGoal());
                                goaladapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        }
                        else
                        {
                            Log.w("==============> ", "Error getting documents.", task.getException());
                            progressDialog.dismiss();
                        }
                    }
                });
        //ADDING GOAL TO LIST (MAKE LIST INVISIBLE)
        goalList.setAdapter(goaladapter);

      //SELECT A CATEGORY
      categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String selection = onlyCategories.get(position);
                Integer goalselection = onlyGoals.get(position);
                SelectCategory.message = Integer.parseInt(goalselection.toString());
                Intent i = new Intent(SelectCategory.this, AddItems.class);
                i.putExtra("cat_type", selection);
               // i.putExtra("goal_type", goalselection);
                startActivity(i);
            }
        });
    }
}
