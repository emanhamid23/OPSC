package com.varsitycollege.navbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    TextView text1, text2;
    Button btnAdd, backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        String name = getIntent().getStringExtra("NAME");
        String goal = getIntent().getStringExtra("GOAL");

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        btnAdd = findViewById(R.id.btnAddItem);
        backbutton = findViewById(R.id.backbutton);

        text1.setText(name);
        text2.setText(goal);
        btnAdd = (Button) findViewById(R.id.btnAddItem);

        //BACK BUTTON
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, CategoryList.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this, AddItems.class));
            }
        });
    }
}