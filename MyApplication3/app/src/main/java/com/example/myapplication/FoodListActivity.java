package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
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
import com.nightonke.boommenu.Util;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class FoodListActivity extends AppCompatActivity {

    BoomMenuButton nutsPulsesGrainsBtn,fruVegBerBtn,fishMeatEggsBtn,bmb,bmb2;
    Activity currentActivity;
    FirebaseAuth mAuth;
    String id;
    TextView nameTextView,sumOfCoinsView;
    ImageView profileImageView,logoImageView;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        initViews();
        initGeneral();
        hideActionBar();
        userNameAndProfileImageView();
        menuMaker();
        foodListMaker();
        sumOfCoinsViewSetValue();

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
        if (id!=null)
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user=snapshot.getValue(User.class);

                    nameTextView.setText(user.getUserName());
                    if (user.userImagePath.equals(""))//If there is no image set a default image
                        profileImageView.setImageResource(R.drawable.profile_pic);
                    else
                    Picasso.get().load(user.getUserImagePath()).into(profileImageView);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    private void foodListMaker() {
        nutsPulsesGrainsBtn.setButtonEnum(ButtonEnum.TextInsideCircle);
        fruVegBerBtn.setButtonEnum(ButtonEnum.TextInsideCircle);
        fishMeatEggsBtn.setButtonEnum(ButtonEnum.TextInsideCircle);
        String titles[] = { "Almonds", "Brazil Nuts", "Lentils", "Oatmeal","Wheat Germ"};
        String titles2[]={"Broccoli","Apples",  "Kale", "Blueberries","Avocados","Leafy Green Vegetables","Sweet Potatoes"};
        String titles3[] = { "Oily Fish", "Chicken", "Eggs"};
        int colors[]={R.color.green,R.color.purple_1000,R.color.green,R.color.purple_1000,R.color.green,R.color.purple_1000,R.color.green};


         for (int i = 0; i < nutsPulsesGrainsBtn.getPiecePlaceEnum().pieceNumber(); i++) {
             TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                     .listener(new OnBMClickListener() {
                         @Override
                         public void onBoomButtonClick(int index) {
                             // When the boom-button corresponding this builder is clicked.

                             switch (index) {
                                 case 0:
                                     startActivity(new Intent(currentActivity, AlmondsActivity.class));
                                     break;
                                 case 1:
                                     startActivity(new Intent(currentActivity, BrazilNutsActivity.class));
                                     break;
                                 case 2:
                                     startActivity(new Intent(currentActivity, LentilsActivity.class));
                                     break;
                                 case 3:
                                     startActivity(new Intent(currentActivity, OatmealActivity.class));
                                     break;
                                 case 4:
                                     startActivity(new Intent(currentActivity, WheatGermActivity.class));
                                     break;

                             }


                         }
                     }).normalText(titles[i]).normalColor(Color.WHITE).normalTextColorRes(colors[i]).typeface(Typeface.DEFAULT_BOLD).rotateText(false).textSize(12).textRect(new Rect(Util.dp2px(15), Util.dp2px(8), Util.dp2px(65), Util.dp2px(72))).maxLines(2);


             nutsPulsesGrainsBtn.addBuilder(builder);
         }

             for (int i = 0; i < fruVegBerBtn.getPiecePlaceEnum().pieceNumber(); i++) {
                 TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                         .listener(new OnBMClickListener() {
                             @Override
                             public void onBoomButtonClick(int index) {
                                 // When the boom-button corresponding this builder is clicked.

                                 switch (index) {
                                     case 0:
                                         startActivity(new Intent(currentActivity, BroccoliActivity.class));
                                         break;
                                     case 1:
                                         startActivity(new Intent(currentActivity, ApplesActivity.class));
                                         break;
                                     case 2:
                                         startActivity(new Intent(currentActivity, KaleActivity.class));
                                         break;
                                     case 3:
                                         startActivity(new Intent(currentActivity, BlueberriesActivity.class));
                                         break;
                                     case 4:
                                         startActivity(new Intent(currentActivity, AvocadosActivity.class));
                                         break;
                                     case 5:
                                         startActivity(new Intent(currentActivity, LeafyGreenVegetablesActivity.class));
                                         break;
                                     case 6:
                                         startActivity(new Intent(currentActivity, SweetPotatoesActivity.class));
                                         break;


                                 }


                             }
                         }).normalText(titles2[i]).normalColor(Color.WHITE).normalTextColorRes(colors[i]).typeface(Typeface.DEFAULT_BOLD).rotateText(false).textSize(12).textRect(new Rect(Util.dp2px(15), Util.dp2px(8), Util.dp2px(65), Util.dp2px(72))).maxLines(2);


                 fruVegBerBtn.addBuilder(builder);
             }

        for (int i = 0; i < fishMeatEggsBtn.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.

                            switch (index) {
                                case 0:
                                    startActivity(new Intent(currentActivity, OilyFishActivity.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(currentActivity, ChickenActivity.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(currentActivity, EggsActivity.class));
                                    break;

                            }


                        }
                    }).normalText(titles3[i]).normalColor(Color.WHITE).normalTextColorRes(colors[i]).typeface(Typeface.DEFAULT_BOLD).rotateText(false).textSize(12).textRect(new Rect(Util.dp2px(15), Util.dp2px(8), Util.dp2px(65), Util.dp2px(72))).maxLines(2);


            fishMeatEggsBtn.addBuilder(builder);
        }
         }
    private void initGeneral() {
     mAuth=FirebaseAuth.getInstance();
     id=mAuth.getUid();
     currentActivity=FoodListActivity.this;
     database=FirebaseDatabase.getInstance();
     myRef= database.getReference("Users").child(id);
    }
    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }
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
    private void sumOfCoinsViewSetValue() {
        myRef.child("Coins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.hasChildren()) {

                    Map<String, String> value = (Map<String, String>) snapshot.getValue();

                    sumOfCoinsView.setText(value.values().toArray()[0].toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }//Show the updated sum of coins
    private void initViews() {
        nutsPulsesGrainsBtn=findViewById(R.id.nutsPulsesGrainsBtn);
        fruVegBerBtn=findViewById(R.id.fruVegBerBtn);
        fishMeatEggsBtn=findViewById(R.id.fishMeatEggsBtn);
        bmb=findViewById(R.id.boomBtn);
        bmb2=findViewById(R.id.boomBtn2);
        nameTextView=findViewById(R.id.nameTextView);
        profileImageView=findViewById(R.id.profileImageView);
        sumOfCoinsView=findViewById(R.id.sumOfCoinsView);
        logoImageView=findViewById(R.id.logoImageView);
    }

}