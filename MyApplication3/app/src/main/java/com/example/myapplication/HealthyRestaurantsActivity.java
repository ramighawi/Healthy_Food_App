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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class HealthyRestaurantsActivity extends AppCompatActivity implements View.OnClickListener,OnImageClickListener{
    BoomMenuButton bmb,bmb2;
    RecyclerView listView;
    TextView bigTextView,smallTextView,nameTextView,sumOfCoinsView;
    ImageView restaurantImage,profileImageView,logoImageView;
    RestaurantListAdapter restaurantListAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef,myRefUser,myFavRef;

    ArrayList<Restaurant> restaurantList;
    FloatingActionButton addRestaurantBtn;
    Activity currentActivity;


    FirebaseAuth mAuth;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_restaurants);



        initViews();
        initGeneral();
        initButtons();
        hideActionBar();
        userNameAndProfileImageView();
        menuMaker();
        readFromDB();
        sumOfCoinsViewSetValue();


        listView.setLayoutManager(new CardSliderLayoutManager(this));
        new CardSnapHelper().attachToRecyclerView(listView);
        listView.setHasFixedSize(false);



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

    private void initGeneral() {
        mAuth=FirebaseAuth.getInstance();
        id=mAuth.getUid();
        currentActivity=HealthyRestaurantsActivity.this;
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Restaurants");
        myRefUser=database.getReference("Users").child(id);
        myFavRef=database.getReference("Users/"+id+"/My Favorite Restaurants");
        restaurantList=new ArrayList<>();

           }
    private void userNameAndProfileImageView() {
        if (id!=null)


        myRefUser.addValueEventListener(new ValueEventListener() {
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
    private void sumOfCoinsViewSetValue() {
        myRefUser.child("Coins").addValueEventListener(new ValueEventListener() {
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
    private void readFromDB() {


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dp:snapshot.getChildren()){
                    Restaurant temp= dp.getValue(Restaurant.class);

                    restaurantList.add(temp);


                }
                restaurantListAdapter=new RestaurantListAdapter(HealthyRestaurantsActivity.this, restaurantList);

                listView.setHasFixedSize(true);

                listView.setAdapter(restaurantListAdapter);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }//Reading from database using adapter and arraylist
    private void initButtons() {

        addRestaurantBtn.setOnClickListener(this);
    }
    private void initViews() {

        addRestaurantBtn= findViewById(R.id.addRestaurantBtn);
        bigTextView=findViewById(R.id.bigText);
        smallTextView=findViewById(R.id.smallText);
        restaurantImage=findViewById(R.id.restaurantImage);
        profileImageView=findViewById(R.id.profileImageView);
        logoImageView=findViewById(R.id.logoImageView);
        bmb = findViewById (R.id.boomBtn3);
        bmb2=findViewById(R.id.boomBtn2);
        nameTextView=findViewById(R.id.nameTextView);
        sumOfCoinsView=findViewById(R.id.sumOfCoinsView);
        listView=findViewById(R.id.restaurantView);


    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.addRestaurantBtn)
            startActivity(new Intent(HealthyRestaurantsActivity.this,AddingRestaurantsActivity.class));
    }

    @Override
    public void onImageClick(String image, String bigText, String smallText) {//Show the clicked image from the list with its details

       bigTextView.setText(bigText);
       smallTextView.setText(smallText);
        Picasso.get().load(image).into(restaurantImage);


    }


}