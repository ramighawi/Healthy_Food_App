package com.example.myapplication;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText userEmail,userPassword;
    ImageView logBtn,signUpView;
    FirebaseAuth mAuth;
    TextView forgotPasswordText;

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser()!=null)//If user is connected, go to welcome activity (home page)
            startActivity(new Intent(MainActivity.this,WelcomeActivity.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();

        hideActionBar();
        initViews();
        resetPassword();
        onClickListeners();

        forgotPasswordText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);//Textview underline




    }

    private void onClickListeners() {
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        signUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
            }
        });
    }

    private void resetPassword() {
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (TextUtils.isEmpty(userEmail.getText().toString()))//Ensure that email was written to send a message for a new password
                {
                    Toast.makeText(getApplicationContext(),"Please Insert Email!",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    mAuth.sendPasswordResetEmail(userEmail.getText().toString())//Sending a password reset and showing a message when task is complete
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Email Sent!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });}
            }
        });

    }

    private void initViews() {
        userEmail=findViewById(R.id.userEmail);
        userPassword=findViewById(R.id.userPassword);
        logBtn=findViewById(R.id.logBtn);
        signUpView=findViewById(R.id.signupBtn);
        forgotPasswordText=findViewById(R.id.forgotPasswordText);
    }

    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }

    private void signInUser() {
        if  (TextUtils.isEmpty(userEmail.getText().toString()))//Ensure that email field is not empty
        {
            Toast.makeText(getApplicationContext(),"Please Insert Email!",Toast.LENGTH_LONG).show();
            return;
        }
        if  (TextUtils.isEmpty(userPassword.getText().toString()))//Ensure that password field is not empty
        {
            Toast.makeText(getApplicationContext(),"Please Insert Password!",Toast.LENGTH_LONG).show();
            return;
        }


        //Sign in with email and password
        mAuth.signInWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if (task.isSuccessful())//If task was successful go to welcome activity (home page)
                {
                    startActivity(new Intent(MainActivity.this,WelcomeActivity.class));
                }
                else//If task is not successful show an exception message
                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}