package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;

public class AddingLunchActivity extends AppCompatActivity {
    FloatingActionButton addBtn;
    EditText kindOfFoodText,amountText,amountKindText;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    boolean alreadyPressed;
    Activity currentActivity;
    String id,currentDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_lunch);

        initViews();
        initGeneral();
        hideActionBar();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDataToDB();

            }
        });

    }

    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }

    private void initGeneral() {
        alreadyPressed=false;
        currentDate=java.text.DateFormat.getDateInstance().format(new Date());
        mAuth=FirebaseAuth.getInstance();
        id=mAuth.getUid();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Users/"+id+"/"+currentDate +"/Lunch");
        currentActivity=AddingLunchActivity.this;
    }

    private void initViews() {
        addBtn=findViewById(R.id.addBtn);
        kindOfFoodText=findViewById(R.id.kindOfFoodText);
        amountText=findViewById(R.id.amountText);
        amountKindText=findViewById(R.id.amountKindText);
    }

    private void addDataToDB() {

        alreadyPressed=false;

        if  (TextUtils.isEmpty(kindOfFoodText.getText().toString()))//Ensure that the field is not empty
        {
            Toast.makeText(getApplicationContext(),"Please Fill The Kind Of Food",Toast.LENGTH_LONG).show();
            return;
        }
        if  (TextUtils.isEmpty(amountText.getText().toString()))//Ensure that the field is not empty
        {
            Toast.makeText(getApplicationContext(),"Please Fill The Amount!",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(amountKindText.getText().toString()))//Ensure that the field is not empty
        {
            Toast.makeText(getApplicationContext(), "Please Choose Gram or Units!", Toast.LENGTH_SHORT).show();
            return;
        }



        Lunch lunch=new Lunch(kindOfFoodText.getText().toString(),amountText.getText().toString(),amountKindText.getText().toString());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (alreadyPressed) return;
                alreadyPressed=true;

                long lunchLength = snapshot.getChildrenCount(); //Length of the menu

                if (lunchLength>=4)//To ensure that menu can include maximum 4 kinds of food
                {
                    Toast.makeText(getApplicationContext(), "Menu Is Full!!", Toast.LENGTH_SHORT).show();
                    kindOfFoodText.setText(null);
                    amountKindText.setText(null);
                    amountText.setText(null);
                    return;
                }

                //To ensure there is no duplicates
                for (DataSnapshot dp:snapshot.getChildren()) {
                    Map<String,String> map=(Map<String, String>) dp.getValue();
                    if (map.get("kindOfFood").equals(lunch.kindOfFood))
                    {
                        Toast.makeText(getApplicationContext(),"Food Exist!!",Toast.LENGTH_LONG).show();
                        kindOfFoodText.setText("");
                        amountText.setText("");
                        amountKindText.setText("");
                        return;
                    }}

                myRef.push().setValue(lunch);
                startActivity(new Intent(currentActivity,MyDailyListActivity.class));


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}