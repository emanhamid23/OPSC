package com.varsitycollege.navbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class ItemsActivity extends AppCompatActivity {
    TextView text1, text2, text5, text7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        String category = getIntent().getStringExtra("CATEGORY");
        String item = getIntent().getStringExtra("ITEM");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String date = getIntent().getStringExtra("DATE");

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text5 = findViewById(R.id.text5);
        text7 = findViewById(R.id.text7);

        text1.setText(category);
        text2.setText(item);
        text5.setText(description);
        text7.setText(date);
    }
}


