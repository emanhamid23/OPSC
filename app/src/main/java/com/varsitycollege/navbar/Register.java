package com.varsitycollege.navbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    //CONNECT TO FIREBASE - FIRE STORE
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    //GETTING ALL VALUES
    EditText SU_username, SU_password, SU_ConfirmPass;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    //START
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //FIND VIEW BY ID
        SU_username =findViewById(R.id.SU_username);
        SU_password =findViewById(R.id.SU_password);
        SU_ConfirmPass =findViewById(R.id.SU_confirmpass);
        btnRegister =findViewById(R.id.btn_signup2);

        //CONNECT TO FIRE STORE
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //REGISTER BUTTON
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                PerforAuth();
            }
        });
    }

    //REGISTER USER
    private void PerforAuth()
    {
        String email = SU_username.getText().toString();
        String password = SU_password.getText().toString();
        String confirmPassword = SU_ConfirmPass.getText().toString();

        if(!email.matches(emailPattern))
        {
            SU_username.setError("Enter a valid email!");
        }
        else if(password.isEmpty() || password.length()<6)
        {
            SU_password.setError("Enter a correct password!");
        }
        else if(!password.equals(confirmPassword))
        {
            SU_ConfirmPass.setError("Password does not match!");
        }
        else
        {
            progressDialog.setMessage("Please wait while registering. . .");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Intent i = new Intent(Register.this, LogIn.class);
            startActivity(i);

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Registration Unsuccessful, Try again."+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}