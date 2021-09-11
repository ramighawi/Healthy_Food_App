package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Map;

public class PrizeDeliveryActivity extends AppCompatActivity {
    ImageView prizeImageView,sendDeliveryImageView;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    TextView nameTextView,emailTextView,phoneNumberTextView;
    Activity currentActivity;
    EditText addressEditText;
    String currentDate;
    int decreaseCoins;
    String kindOfPrize;
    boolean alreadyPressed,alreadyPressed2;
    long index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_delivery);

        initViews();
        initGeneral();
        hideActionBar();
        setPrizeImageView();
        userNameAndEmailAndPhoneNumberViews();
        submitDelivery();
    }

    private void submitDelivery() {//To save the delivery details in the database, every user has his own deliveries
        alreadyPressed=false;
        alreadyPressed2=false;


        sendDeliveryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To ensure that address was written
               if (TextUtils.isEmpty(addressEditText.getText().toString())) {
                   Toast.makeText(getApplicationContext(), "Please Insert Your Address", Toast.LENGTH_LONG).show();
                   return;
               }
                databaseRef.child(currentDate).child("Deliveries").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (alreadyPressed) return;
                        alreadyPressed=true;

                            if (snapshot.child("Details").hasChildren())
                                index=snapshot.child("Details").getChildrenCount()+1;//Index for the deliveries
                            else index=1;//the first delivery
                        databaseRef.child(currentDate).child("Deliveries").child("Details").child(String.valueOf(index)).push().setValue(addressEditText.getText().toString());
                        databaseRef.child(currentDate).child("Deliveries").child("Details").child(String.valueOf(index)).push().setValue(nameTextView.getText().toString());
                        databaseRef.child(currentDate).child("Deliveries").child("Details").child(String.valueOf(index)).push().setValue(phoneNumberTextView.getText().toString());
                        databaseRef.child(currentDate).child("Deliveries").child("Details").child(String.valueOf(index)).push().setValue(kindOfPrize);

                        Toast.makeText(getApplicationContext(),"The Prize Is On The Way To You :)",Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                databaseRef.child("Coins").addValueEventListener(new ValueEventListener() {//To ensure that coins will be decreased after getting the prize
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (alreadyPressed2) return;
                        alreadyPressed2=true;
                        if(snapshot.hasChildren()) {
                            Map<String, String> value = (Map<String, String>) snapshot.getValue();

                            int sumOfCoins = Integer.parseInt(value.values().toArray()[0].toString()) -decreaseCoins;//The current value minus the value of the prize

                            String coins = String.valueOf(sumOfCoins);

                            databaseRef.child("Coins").removeValue();
                            databaseRef.child("Coins").push().setValue(coins);
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            
        });
    }
    private void userNameAndEmailAndPhoneNumberViews() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                nameTextView.setText(user.getUserName());
                phoneNumberTextView.setText(user.getUserPhone());
                emailTextView.setText(user.getUserEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initGeneral() {
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseRef=database.getReference("Users").child(mAuth.getUid());
        currentActivity=PrizeDeliveryActivity.this;
        currentDate = java.text.DateFormat.getDateInstance().format(new Date());
    }
    private void setPrizeImageView() {//To set the data of the prize related to the choice, when clicking on the prize in the previous activity
        //an intent will be saved with the data of the prize
        Intent intent = getIntent();
        if (intent != null)
        {

            String clicked = intent.getStringExtra("button");
            switch (clicked){
                case "dumbbellHandOKImageView":
                    prizeImageView.setImageResource(R.drawable.dumbbell_image);
                    decreaseCoins=1000;
                    kindOfPrize="Dumbbells";
                    break;
                case "smartWatchHandOKImageView":
                    prizeImageView.setImageResource(R.drawable.smartwatch_image);
                    decreaseCoins=5000;
                    kindOfPrize="smartWatch";
                    break;
                case  "walkerHandOKImageView":
                    prizeImageView.setImageResource(R.drawable.walker_image);
                    decreaseCoins=10000;
                    kindOfPrize="Walker";
                    break;

            }
        }
    }
    private void initViews() {
        prizeImageView=findViewById(R.id.prizeImageView);
        nameTextView=findViewById(R.id.nameTextView);
        emailTextView=findViewById(R.id.emailTextView);
        phoneNumberTextView=findViewById(R.id.phoneNumberTextView);
        sendDeliveryImageView=findViewById(R.id.sendDeliveryImageView);
        addressEditText=findViewById(R.id.addressEditText);
    }
    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }
}