package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {


    EditText userName,userEmail,userPass,userConfirmPass,userID,userPhone;
    ImageView signupBtn;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        initViews();
        initGeneral();
        hideActionBar();

        signupBtn.setOnClickListener(this);

    }

    private void initGeneral() {
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");
    }

    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }
    private void initViews() {
        userName=findViewById(R.id.userName);
        userEmail=findViewById(R.id.userEmail);
        userPass=findViewById(R.id.userPassword);
        userConfirmPass=findViewById(R.id.userConfirmPassword);
        userPhone=findViewById(R.id.userPhone);
        userID=findViewById(R.id.userID);
        signupBtn=findViewById(R.id.signupBtn);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupBtn:
                signUpUser();
                break;

        }
    }





    private void signUpUser() {
        if(!userPass.getText().toString().equals(userConfirmPass.getText().toString()))//To confirm password
        {
            Toast.makeText(getApplicationContext(),"Passwords Don't Match!",Toast.LENGTH_LONG).show();
            return;
        }
        if  (TextUtils.isEmpty(userEmail.getText().toString()))//To ensure that email field is not empty
        {
            Toast.makeText(getApplicationContext(),"Please Insert Email!",Toast.LENGTH_LONG).show();
            return;
        }
        if  (TextUtils.isEmpty(userPass.getText().toString()))//To ensure that password field is not empty
        {
            Toast.makeText(getApplicationContext(),"Please Insert Password!",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(),userPass.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {

             if (task.isSuccessful())
             {
                 User user=new User(userName.getText().toString(),userPhone.getText().toString()
                 ,userID.getText().toString(),"",userEmail.getText().toString());
                 myRef.child(mAuth.getUid()).setValue(user);
                 startActivity(new Intent(SignupActivity.this,MainActivity.class));

             }
             else
             {
                 Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
             }
            }
        });
    }
}