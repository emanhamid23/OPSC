package com.varsitycollege.navbar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import models.Category;
import models.Data;

public class Showitems extends AppCompatActivity {

    //CONNECT TO FIREBASE - FIRE STORE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

    //GETTING ALL VALES
    TextView showtxt, getcatname, setitems, getgoal2;
    EditText text;
    ListView dataList;
    Button selectitem, backbtn, viewitemdetails;

    public String selectedItem;

    //ARRAY TO STORE GOALS
    ArrayList<Data> datas = new ArrayList<>();

    //START
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showitems);

        //FIND VIEW BY ID
        getgoal2 = findViewById(R.id.getgoal2);
        viewitemdetails = findViewById(R.id.viewitemdetails);
        dataList = findViewById(R.id.itemList);
        backbtn = findViewById(R.id.backbtn);
        text = findViewById(R.id.textview);
        getcatname = (TextView)findViewById(R.id.getcatname);

        //GETTING VALUE(CATEGORY NAME) FROM OTHER PAGE
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        getcatname.setText(str);

        //ARRAY TO DISPLAY ITEMS LIST
        ArrayList<String> onlyData = new ArrayList<>();

        Toast.makeText(this, getcatname.getText(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, onlyData);
        db.collection("data")
                .whereEqualTo("category", getcatname.getText())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d("DATA", "onComplete: " + document.getData());
                                String name = String.valueOf(document.getData().get("item"));
                                String date = String.valueOf(document.getData().get("dateChoosen"));
                                String categoryname = String.valueOf(document.getData().get("category"));
                                String itemdescription = String.valueOf(document.getData().get("descriptionItem"));

                                Data dat = new Data(name, date, categoryname, itemdescription);    //I ADDED THE KEY HERE
                                datas.add(dat);
                            }
                            for (Data x: datas)
                            {
                                onlyData.add(x.getName());
                                adapter.notifyDataSetChanged();
                                int num = dataList.getAdapter().getCount();
                                text.setText(String.valueOf(num));
                            }
                        } else
                        {
                            Log.w("==============> ", "Error getting documents.", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(Showitems.this, "Fetching Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        //ADDING DATA TO LIST
        dataList.setAdapter(adapter);

        //BACK BUTTON
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        //CLICK BUTTON TO VIEW DETAILS ABOUT ITEMS
        viewitemdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = getcatname.getText().toString();
               // Integer str3 = Integer.parseInt(getgoal2.getText().toString());
                Intent intent = new Intent(getApplicationContext(), ItemDetails.class);
                intent.putExtra("message_key2", str);
               // intent.putExtra("get_goal", str3);
                intent.putExtra("key", text.getText().toString());
                // start the Intent
                startActivity(intent);
            }
        });
    }
}