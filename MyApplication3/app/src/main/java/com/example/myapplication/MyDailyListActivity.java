package com.example.myapplication;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MyDailyListActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton breakfastAddBtn, lunchAddBtn, dinnerAddBtn;
    BoomMenuButton bmb, bmb2;
    ListView breakfastList, lunchList, dinnerList;
    TextView dateView, userPlanForTodayView, nameTextView, sumOfCoinsView;
    ImageView profileImageView, logoImageView;
    CheckBox breakfastCheckBox, lunchCheckBox, dinnerCheckBox;
    String currentDateTimeString,currentDate;

    BreakfastAdapter breakfastAdapter;
    LunchAdapter lunchAdapter;
    DinnerAdapter dinnerAdapter;

    FirebaseDatabase database;
    DatabaseReference myRefBreakfast, myRefLunch, myRefDinner, myRef;

    ArrayList<Breakfast> breakfasts;
    ArrayList<Lunch> lunches;
    ArrayList<Dinner> dinners;

    Activity currentActivity;

    FirebaseAuth mAuth;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_daily_list);


        initViews();
        initButton();
        initGeneral();
        hideActionBar();
        viewsOnClickListeners();//Includes profile and logo images
        menuMaker();
        userNameAndProfileImageView();
        planForTodayView();
        planForTodayAnimation();

        dateView.setText(currentDateTimeString);

        readFromDB();

        breakfastCheckBoxValue();//To ensure that every user has his own checkbox status
        lunchCheckBoxValue();
        dinnerCheckBoxValue();

        breakfastLastCheckedDate();//To ensure that day after the checkbox will be initialized
        lunchLastCheckedDate();
        dinnerLastCheckedDate();

        if (breakfastCheckBox.isChecked())//To ensure that checkbox can be checked just one time in 24 hours
            breakfastCheckBox.setClickable(false);

        if (lunchCheckBox.isChecked())
            lunchCheckBox.setClickable(false);

        if (dinnerCheckBox.isChecked())
            dinnerCheckBox.setClickable(false);

        breakfastCheckBoxOnClick();
        lunchCheckBoxOnClick();
        dinnerCheckBoxOnClick();
        sumOfCoinsViewSetValue();


    }
    private void breakfastCheckBoxOnClick() {
        breakfastCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                breakfastCheckBox.setChecked(false);

                myRef.child("Coins").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (breakfastCheckBox.isChecked()) {

                            return;
                        }

                        if(snapshot.hasChildren()) {//If the user had checked and have coins(coins is not null)

                            Map<String, String> value = (Map<String, String>) snapshot.getValue();

                            int sumOfCoins = Integer.parseInt(value.values().toArray()[0].toString()) + 5;//Get the current coins and add 5

                            String coins = String.valueOf(sumOfCoins);

                            myRef.child("Coins").removeValue();//Remove the previous sum of coins
                            myRef.child("Coins").push().setValue(coins);//Set the new value of coins


                            myRef.child("breakfastChecked").removeValue();//Remove the previous status of the checkbox
                            myRef.child("breakfastChecked").push().setValue("true");//Set the new status of the checkbox// to ensure that every user
                            //get his own status

                            myRef.child("breakfastLastChecked").removeValue();//Remove the previous date of checked box
                            myRef.child("breakfastLastChecked").push().setValue(currentDate);//Set the new date of checked box// to ensure that day after
                            //the status of the check box will be not checked


                            breakfastCheckBox.setChecked(true);//The new status after click is done
                            breakfastCheckBox.setClickable(false);//The new status after click is done

                        } else {//If this is the first time he checks the boxes
                            myRef.child("Coins").push().setValue("5");

                            breakfastCheckBox.setChecked(true);
                            breakfastCheckBox.setClickable(false);

                            myRef.child("breakfastChecked").removeValue();
                            myRef.child("breakfastChecked").push().setValue("true");

                            myRef.child("breakfastLastChecked").removeValue();
                            myRef.child("breakfastLastChecked").push().setValue(currentDate);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private void lunchCheckBoxOnClick() {
        lunchCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                lunchCheckBox.setChecked(false);

                myRef.child("Coins").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (lunchCheckBox.isChecked()) {

                            return;
                        }


                        if(snapshot.hasChildren()) {
                            Map<String, String> value = (Map<String, String>) snapshot.getValue();

                            int sumOfCoins = Integer.parseInt(value.values().toArray()[0].toString()) + 5;

                            String coins = String.valueOf(sumOfCoins);

                            myRef.child("Coins").removeValue();
                            myRef.child("Coins").push().setValue(coins);


                            myRef.child("lunchChecked").removeValue();

                            myRef.child("lunchChecked").push().setValue("true");
                            myRef.child("lunchLastChecked").removeValue();
                            myRef.child("lunchLastChecked").push().setValue(currentDate);


                            lunchCheckBox.setChecked(true);
                            lunchCheckBox.setClickable(false);

                        } else {
                            myRef.child("Coins").push().setValue("5");
                            lunchCheckBox.setChecked(true);
                            lunchCheckBox.setClickable(false);
                            myRef.child("lunchChecked").removeValue();
                            myRef.child("lunchChecked").push().setValue("true");
                            myRef.child("lunchLastChecked").removeValue();
                            myRef.child("lunchLastChecked").push().setValue(currentDate);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private void dinnerCheckBoxOnClick() {
        dinnerCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dinnerCheckBox.setChecked(false);

                myRef.child("Coins").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (dinnerCheckBox.isChecked()) {

                            return;
                        }


                        if(snapshot.hasChildren()) {
                            Map<String, String> value = (Map<String, String>) snapshot.getValue();

                            int sumOfCoins = Integer.parseInt(value.values().toArray()[0].toString()) + 5;

                            String coins = String.valueOf(sumOfCoins);

                            myRef.child("Coins").removeValue();
                            myRef.child("Coins").push().setValue(coins);


                            myRef.child("dinnerChecked").removeValue();

                            myRef.child("dinnerChecked").push().setValue("true");
                            myRef.child("dinnerLastChecked").removeValue();
                            myRef.child("dinnerLastChecked").push().setValue(currentDate);


                            dinnerCheckBox.setChecked(true);
                            dinnerCheckBox.setClickable(false);

                        } else {
                            myRef.child("Coins").push().setValue("5");
                            dinnerCheckBox.setChecked(true);
                            dinnerCheckBox.setClickable(false);
                            myRef.child("dinnerChecked").removeValue();
                            myRef.child("dinnerChecked").push().setValue("true");
                            myRef.child("dinnerLastChecked").removeValue();
                            myRef.child("dinnerLastChecked").push().setValue(currentDate);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void breakfastLastCheckedDate() {
        myRef.child("breakfastLastChecked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren()) {
                    Map<String, String> value = (Map<String, String>) snapshot.getValue();

                    if (value.values().toArray()[0].toString().equals(currentDate)) {
                        return;
                    } else {
                        breakfastCheckBox.setClickable(true);
                        breakfastCheckBox.setChecked(false);
                        if (breakfastCheckBox.isChecked()) {

                            myRef.child("breakfastLastChecked").removeValue();
                            myRef.child("breakfastLastChecked").push().setValue(currentDate);

                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void lunchLastCheckedDate() {
        myRef.child("lunchLastChecked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String lastDate;
                if (snapshot.hasChildren()) {
                    Map<String,String> value=(Map<String, String>)snapshot.getValue();
                    lastDate = value.values().toArray()[0].toString();
                    if (lastDate.equals(currentDate)) {
                        return;
                    } else {
                        lunchCheckBox.setClickable(true);
                        lunchCheckBox.setChecked(false);
                        if (lunchCheckBox.isChecked()) {

                            myRef.child("lunchLastChecked").removeValue();
                            myRef.child("lunchLastChecked").push().setValue(currentDate);

                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void dinnerLastCheckedDate() {
        myRef.child("dinnerLastChecked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String lastDate;
                if (snapshot.hasChildren()) {
                    Map<String,String> value=(Map<String, String>) snapshot.getValue();
                    lastDate = value.values().toArray()[0].toString();
                    if (lastDate.equals(currentDate)) {
                        return;
                    } else {
                        dinnerCheckBox.setClickable(true);
                        dinnerCheckBox.setChecked(false);
                        if (dinnerCheckBox.isChecked()) {

                            myRef.child("dinnerLastChecked").removeValue();
                            myRef.child("dinnerLastChecked").push().setValue(currentDate);

                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void breakfastCheckBoxValue() {
        myRef.child("breakfastChecked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isChecked;
                if (snapshot.hasChildren()) {
                    Map<String,String> value= (Map<String, String>) snapshot.getValue();

                    isChecked = value.values().toArray()[0].toString();


                    if (isChecked.equals("true")) {
                        breakfastCheckBox.setChecked(true);
                        breakfastCheckBox.setClickable(false);
                    } else
                        breakfastCheckBox.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void lunchCheckBoxValue() {
        myRef.child("lunchChecked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isChecked;
                if (snapshot.hasChildren()) {
                    Map<String,String> value=(Map<String, String>)snapshot.getValue();
                    isChecked = value.values().toArray()[0].toString();
                    if (isChecked.equals("true")) {
                        lunchCheckBox.setChecked(true);
                        lunchCheckBox.setClickable(false);
                    } else
                        lunchCheckBox.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void dinnerCheckBoxValue() {
        myRef.child("dinnerChecked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isChecked;
                if (snapshot.hasChildren()) {
                    Map<String,String> value=(Map<String, String>) snapshot.getValue();
                    isChecked = value.values().toArray()[0].toString();

                    if (isChecked.equals("true")) {
                        dinnerCheckBox.setChecked(true);
                        dinnerCheckBox.setClickable(false);
                    } else
                        dinnerCheckBox.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sumOfCoinsViewSetValue() {//Show the updated sum of coins
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

    }

    private void viewsOnClickListeners() {
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity, ProfileActivity.class));
            }
        });

        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity, WelcomeActivity.class));
            }
        });


    }

    private void userNameAndProfileImageView() {
        if (id != null)

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                nameTextView.setText(user.getUserName());//User name
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

    private void planForTodayView() {
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);


                if (user != null) {
                    userPlanForTodayView.setText(MessageFormat.format("Hey {0}\n Plan Your Healthy Food For Today", user.getUserName()));

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void initGeneral() {
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getUid();
        currentDate = java.text.DateFormat.getDateInstance().format(new Date());
        currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        currentActivity = MyDailyListActivity.this;
        database = FirebaseDatabase.getInstance();
        myRefBreakfast = database.getReference("Users/" + id + "/" + currentDate + "/Breakfast");
        myRefLunch = database.getReference("Users/" + id + "/" + currentDate + "/Lunch");
        myRefDinner = database.getReference("Users/" + id + "/" + currentDate + "/Dinner");
        myRef = database.getReference("Users").child(id);
        breakfasts = new ArrayList<>();
        lunches = new ArrayList<>();
        dinners = new ArrayList<>();
    }

    private void planForTodayAnimation() {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 360f);
        //repeats the animation 1 time
        valueAnimator.setRepeatCount(1);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
        // animate over the course of 2500 milliseconds
        valueAnimator.setDuration(2500);
        // define how to update the view at each "step" of the animation
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                userPlanForTodayView.setRotationY(progress);

            }
        });
        valueAnimator.start();
    }

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
                        startActivity(new Intent(currentActivity, MainActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    private void readFromDB() {
        myRefBreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                for (DataSnapshot dp : snapshot.getChildren()) {
                    Breakfast temp = dp.getValue(Breakfast.class);

                    breakfasts.add(temp);

                }
                breakfastAdapter = new BreakfastAdapter(currentActivity, breakfasts);
                breakfastList.setAdapter(breakfastAdapter);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        myRefLunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dp : snapshot.getChildren()) {
                    Lunch temp = dp.getValue(Lunch.class);
                    lunches.add(temp);
                }
                lunchAdapter = new LunchAdapter(MyDailyListActivity.this, lunches);
                lunchList.setAdapter(lunchAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        myRefDinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dp : snapshot.getChildren()) {
                    Dinner temp = dp.getValue(Dinner.class);
                    dinners.add(temp);
                }

                dinnerAdapter = new DinnerAdapter(MyDailyListActivity.this, dinners);
                dinnerList.setAdapter(dinnerAdapter);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }//Reading from database using adapter and arraylist

    private void initViews() {

        breakfastAddBtn = findViewById(R.id.breakfastAddBtn);
        lunchAddBtn = findViewById(R.id.lunchAddBtn);
        dinnerAddBtn = findViewById(R.id.dinnerAddBtn);
        breakfastList = findViewById(R.id.breakfastList);
        lunchList = findViewById(R.id.lunchList);
        dinnerList = findViewById(R.id.dinnerList);
        dateView = findViewById(R.id.dateView);
        userPlanForTodayView = findViewById(R.id.userPlanForToday);
        bmb = findViewById(R.id.boomBtn3);
        bmb2 = findViewById(R.id.boomBtn2);
        profileImageView = findViewById(R.id.profileImageView);
        logoImageView = findViewById(R.id.logoImageView);
        nameTextView = findViewById(R.id.nameTextView);
        breakfastCheckBox = findViewById(R.id.breakfastCheckBox);
        lunchCheckBox = findViewById(R.id.lunchCheckBox);
        dinnerCheckBox = findViewById(R.id.dinnerCheckBox);
        sumOfCoinsView = findViewById(R.id.sumOfCoinsView);
    }

    private void initButton() {
        breakfastAddBtn.setOnClickListener(this);
        lunchAddBtn.setOnClickListener(this);
        dinnerAddBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.breakfastAddBtn)

            startActivity(new Intent(MyDailyListActivity.this, AddingBreakfastActivity.class));

        if (v.getId() == R.id.lunchAddBtn)
            startActivity(new Intent(MyDailyListActivity.this, AddingLunchActivity.class));

        if (v.getId() == R.id.dinnerAddBtn)
            startActivity(new Intent(MyDailyListActivity.this, AddingDinnerActivity.class));


    }


}

