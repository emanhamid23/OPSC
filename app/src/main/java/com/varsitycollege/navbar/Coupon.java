package com.varsitycollege.navbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Coupon extends AppCompatActivity {

    TextView coupon_instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        coupon_instructions = findViewById(R.id.coupon_instructions);

        coupon_instructions.setText("Follow the steps to redeem:" + "\n"+ "\n" + "1. User will only receive a coupon when they meet their goal." + "\n" + "2. When user meet their goal, they will be given" +
                " a coupon code to redeem. " + "\n" + "3. User MUST screenshot or save their coupon code. " + "\n" + "4. Users can use the given Coupon code and enter it when buying new items." + "\n"
        + "5. Code will be expired in 72HR.");

    }
}