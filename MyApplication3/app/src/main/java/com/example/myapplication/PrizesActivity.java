package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class PrizesActivity extends AppCompatActivity {
    BoomMenuButton bmb,bmb2 ;
    Activity currentActivity;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    ImageView logoImageView,profileImageView,dumbbellHandOKImageView,smartWatchHandOKImageView,walkerHandOKImageView;
    TextView nameTextView,sumOfCoinsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prizes);

        initViews();
        initGeneral();
        hideActionBar();
        menuMaker();
        sumOfCoinsViewSetValue();
        userNameAndProfileImageView();
        viewsOnClickListeners();


    }
    private void viewsOnClickListeners(){
        dumbbellHandOKImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Set an intent with the name of the clicked button to know which prize has chose

                Intent i = new Intent(currentActivity,PrizeDeliveryActivity.class);
                i.putExtra("button", "dumbbellHandOKImageView");
                startActivity(i);
            }
        });
        smartWatchHandOKImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Set an intent with the name of the clicked button to know which prize has chose

                Intent i = new Intent(currentActivity,PrizeDeliveryActivity.class);
                i.putExtra("button", "smartWatchHandOKImageView");
                startActivity(i);

            }
        });
        walkerHandOKImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Set an intent with the name of the clicked button to know which prize has chose
                Intent i = new Intent(currentActivity,PrizeDeliveryActivity.class);
                i.putExtra("button", "walkerHandOKImageView");
                startActivity(i);
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity,ProfileActivity.class));
            }
        });

        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity,WelcomeActivity.class));
            }
        });

    }
    private void userNameAndProfileImageView() {
        if (mAuth.getUid()!=null)


            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user=snapshot.getValue(User.class);

                    nameTextView.setText(user.getUserName());
                    if(user.userImagePath.equals(""))//If there is no image set a default image
                        profileImageView.setImageResource(R.drawable.profile_pic);
                    else
                        Picasso.get().load(user.getUserImagePath()).into(profileImageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    private void initGeneral() {
        currentActivity=PrizesActivity.this;
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseRef=database.getReference("Users").child(mAuth.getUid());


    }
    private void initViews() {
        bmb=findViewById(R.id.boomBtn);
        bmb2=findViewById(R.id.boomBtn2);
        logoImageView=findViewById(R.id.logoImageView);
        profileImageView=findViewById(R.id.profileImageView);
        nameTextView=findViewById(R.id.nameTextView);
        sumOfCoinsView=findViewById(R.id.sumOfCoinsView);
        dumbbellHandOKImageView=findViewById(R.id.dumbbellHandOKImageView);
        smartWatchHandOKImageView=findViewById(R.id.smartWatchHandOKImageView);
        walkerHandOKImageView=findViewById(R.id.walkerHandOKImageView);
    }
    private void sumOfCoinsViewSetValue() {
        databaseRef.child("Coins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren()) {

                    Map<String, String> value = (Map<String, String>) snapshot.getValue();

                    sumOfCoinsView.setText(value.values().toArray()[0].toString());
                    if (Integer.parseInt(sumOfCoinsView.getText().toString())>=1000)
                        dumbbellHandOKImageView.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(sumOfCoinsView.getText().toString())>=5000)
                        smartWatchHandOKImageView.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(sumOfCoinsView.getText().toString())>=10000)
                        walkerHandOKImageView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }//Show the updated sum of coins
    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }
    private void menuMaker() {

        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb2.setButtonEnum(ButtonEnum.TextOutsideCircle);
        String titles[] = { "Home", "Profile", "About", "Logout"};//Define the titles of the right menu
        String titles2[]={"My Menu","Healthy Restaurants",  "Favorite Restaurants", "Food List"};//Define the titles of the left menu
        int icon[] = {R.mipmap.about_icon,R.mipmap.profile_icon_foreground,R.mipmap.homepage_icon_foreground,R.mipmap.logout};//Define
        //the icons of the right menu
        int icon2[]={ R.mipmap.menu_food_icon,R.mipmap.healthy_icon,R.mipmap.favorite_icon, R.mipmap.foodlist_icon};//Define the icons
        //of the left menu
        int colors[]={R.color.green,R.color.purple_1000,R.color.purple_1000,R.color.green};//Define the text color of the titles of the menu



        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {//Build the right menu including: about , profile,
            // welcome(homepage) activities + logout
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.

                            switch (index){
                                case 0:
                                    startActivity(new Intent(currentActivity, AboutActivity.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(currentActivity, ProfileActivity.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(currentActivity, WelcomeActivity.class));
                                    break;
                                case 3:
                                    logoutUser();

                            }



                        }
                    }).normalImageRes(icon[i]).normalText(titles[i]).normalColor(Color.WHITE);


            bmb.addBuilder(builder);

        }
        for (int i = 0; i < bmb2.getPiecePlaceEnum().pieceNumber(); i++) {//Build the left menu including: my menu (my daily list), healthy restaurants,
            //favorite and food list activities
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.

                            switch (index){
                                case 0:
                                    startActivity(new Intent(currentActivity, MyDailyListActivity.class));
                                    break;
                                case 1:

                                    startActivity(new Intent(currentActivity, HealthyRestaurantsActivity.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(currentActivity, FavoriteActivity.class));
                                    break;
                                case 3:
                                    startActivity(new Intent(currentActivity, FoodListActivity.class));

                                    break;
                            }



                        }
                    }).normalImageRes(icon2[i]).normalText(titles2[i]).normalColor(Color.WHITE).normalTextColorRes(colors[i]).typeface(Typeface.DEFAULT_BOLD).rotateText(false).textWidth(280);
            bmb2.addBuilder(builder);

        }


    }//Boom menu maker (basic menu by external library)
    private void logoutUser() {

        new AlertDialog.Builder(currentActivity)
                .setTitle("Logout")
                .setMessage("Are You Sure You Want To Logout")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(currentActivity,MainActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}