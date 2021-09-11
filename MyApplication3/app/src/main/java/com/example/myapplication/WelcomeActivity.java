package com.example.myapplication;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    TextView welcomeMessage,nameTextView,sumOfCoinsView;
    EditText writeCommentView;
    ImageView logoImageView,profileImageView,logoutImageView,whatsappView,facebookView,instaView,submitCommentImage,coinsView;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseDatabase database;
    DatabaseReference databaseRef,mRef,myChampionsRef;
    Activity currentActivity;
    BoomMenuButton bmb,bmb2 ;
    CommentsAdapter commentsAdapter;
    ListView commentsListView,championsListView;
    Button coinsChampionsBtn,feedsBtn;
    ArrayList <Comments> addCommentsList;
    ArrayList <Champion> addChampionList;
    String email,name,imagePath;

    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
    boolean alreadyPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initViews();
        initButtons();
        initGeneral();

        viewsOnClickListeners();

        userNameAndProfileImageAndWelcomeMessageViews();



        sumOfCoinsViewSetValue();

        hideActionBar();
        menuMaker();
        commentsMaker();

        readCommentsFromDB();


        championsMaker();
        readChampionsFromDB();






    }

    private void userNameAndProfileImageAndWelcomeMessageViews() {

        if (mAuth.getUid()!=null)
            databaseRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        nameTextView.setText(user.getUserName());
                        if (user.getUserImagePath().equals(""))//If there is no image set a default image
                            profileImageView.setImageResource(R.drawable.profile_pic);
                        else
                            Picasso.get().load(user.getUserImagePath()).into(profileImageView);


                        welcomeMessage.setText(MessageFormat.format("Welcome {0}", user.getUserName()));

                        welcomeMessageAnimator();

                    }


                }



                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
    }

    private void welcomeMessageAnimator() {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 360f);
        //repeats the animation 2 times
        valueAnimator.setRepeatCount(2);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
        // animate over the course of 3000 milliseconds
        valueAnimator.setDuration(3000);
        // define how to update the view at each "step" of the animation
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                welcomeMessage.setRotationX(progress);

            }
        });
        valueAnimator.start();
    }


    private void readChampionsFromDB() {

        myChampionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dp:snapshot.getChildren()) {
                    Champion champion = dp.getValue(Champion.class);
                    for (int i = 0; i <addChampionList.size(); i++) {//To ensure there is no duplicates
                        if (champion.getChampionEmail().equals(addChampionList.get(i).getChampionEmail()))
                           return;
                    }

                    addChampionList.add(champion);

                }

                Collections.sort(addChampionList, new Comparator<Champion>() {//Sorting the champion list in descending order

                    /* This comparator will sort objects */

                    @Override
                    public int compare(Champion a1, Champion a2) {

                        // Integer implements Comparable
                        return Integer.compare(Integer.parseInt(a2.championCoins),Integer.parseInt(a1.championCoins));
                    }
                });


                ChampionAdapter championAdapter=new ChampionAdapter(currentActivity,addChampionList);
                 championsListView.setAdapter(championAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }//Reading from database using adapter and arraylist

    private void championsMaker() {
        alreadyPressed = false;

        databaseRef.addValueEventListener(new ValueEventListener() {//Retrieve the user data(email,name,image path)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);

                email=user.getUserEmail();
                name=user.getUserName();
                imagePath=user.getUserImagePath();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myChampionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//building the champion reference with the data retrieved above
                if (alreadyPressed) return;

                Champion champion = new Champion();
                champion.setChampionName(name);
                champion.setChampionImagePath(imagePath);
                champion.setChampionCoins(sumOfCoinsView.getText().toString());
                champion.setChampionEmail(email);
                if (email==null) {
                    alreadyPressed=true;
                    return;
                }
                for (DataSnapshot dp : snapshot.getChildren()) {
                        Champion temp = dp.getValue(Champion.class);

                                if (temp.getChampionEmail().equals(email)) {
                                    if (dp.getKey() != null) {
                                        myChampionsRef.child(dp.getKey()).removeValue();
                                        myChampionsRef.push().setValue(champion);
                                        alreadyPressed=true;
                                        break;

                                    }
                                }
                            }
                if (!alreadyPressed)
                {

                    myChampionsRef.push().setValue(champion);
                    alreadyPressed=true;
                }

            }


                @Override
                public void onCancelled (@NonNull DatabaseError error){

                 }

         });


     }


    private void commentsMaker() {


              submitCommentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//To ensure that comments cant be empty
                if (TextUtils.isEmpty(writeCommentView.getText().toString())){
                    Toast.makeText(currentActivity, "Please Write Something!!!",
                Toast.LENGTH_SHORT).show();
                    return;}
                alreadyPressed = false;


                mRef.addValueEventListener(new ValueEventListener() {//building the comments reference including the data that mentioned below
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Comments comment=new Comments();
                            databaseRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user=snapshot.getValue(User.class);
                                     if (alreadyPressed) return;
                                      alreadyPressed=true;

                                      comment.setCommentMakerUID(mAuth.getUid());
                                      comment.setCommentText(writeCommentView.getText().toString());
                                      comment.setCommentDateTime(currentDateTimeString);
                                      comment.setCommentMakerName(user.userName);

                                      mRef.push().setValue(comment);
                                      finish();
                                      startActivity(getIntent());


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void readCommentsFromDB() {


            mRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Comments comment = dataSnapshot.getValue(Comments.class);


                        addCommentsList.add(comment);

                    }

                    commentsAdapter = new CommentsAdapter(currentActivity, addCommentsList);

                    commentsListView.setAdapter(commentsAdapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



    }//Reading from database using adapter and arraylist

    private void initGeneral() {
        currentActivity=WelcomeActivity.this;
        database = FirebaseDatabase.getInstance();
        mRef=database.getReference("Comments");
        databaseRef=database.getReference("Users").child(mAuth.getUid());
        myChampionsRef=database.getReference("Champions");
        addCommentsList=new ArrayList<>();
        addChampionList=new ArrayList<>();

    }

    private void viewsOnClickListeners() {
        coinsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentActivity,PrizesActivity.class));
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

        logoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        //Show my account in instagram
        instaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/");


                Intent i= new Intent(Intent.ACTION_VIEW,uri);

                i.setPackage("com.instagram.android");

                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.instagram.android")));//go to market if don't have instagram app
                }
            }
        });

        //Send me a whatsapp message
        whatsappView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://api.whatsapp.com/send?phone="+"972508540997";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        //Show my facebook account
        facebookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = getFBIntent(getApplicationContext(), "2347633432");
                startActivity(facebookIntent);

            }
        });


    }
    public Intent getFBIntent(Context context, String facebookId) {

        try {
            // Check if FB app is even installed
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);

            String facebookScheme = "fb://profile/" + facebookId;
            return new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
        }
        catch(Exception e) {

            // Cache and Open a url in browser
            String facebookProfileUri = "https://www.facebook.com/" + facebookId;
            return new Intent(Intent.ACTION_VIEW, Uri.parse(facebookProfileUri));
        }


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
                        startActivity(new Intent(currentActivity,MainActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    private void initButtons() {
        coinsChampionsBtn.setOnClickListener(this);
        feedsBtn.setOnClickListener(this);


    }
    private void sumOfCoinsViewSetValue() {
        databaseRef.child("Coins").addValueEventListener(new ValueEventListener() {
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
        welcomeMessage=findViewById(R.id.welcomeMessage);
        nameTextView=findViewById(R.id.nameTextView);
        logoImageView=findViewById(R.id.logoImageView);
        profileImageView=findViewById(R.id.profileImageView);
        logoutImageView=findViewById(R.id.logoutImageView);
        bmb = findViewById (R.id.boomBtn3);
        bmb2=findViewById(R.id.boomBtn2);
        whatsappView=findViewById(R.id.whatsappIcon);
        facebookView=findViewById(R.id.facebookIcon);
        instaView=findViewById(R.id.instaIcon);
        sumOfCoinsView=findViewById(R.id.sumOfCoinsView);
        writeCommentView=findViewById(R.id.writeCommentView);
        commentsListView=findViewById(R.id.commentsListView);
        submitCommentImage=findViewById(R.id.submitCommentImage);
        coinsChampionsBtn=findViewById(R.id.coinsChampionsBtn);
        feedsBtn=findViewById(R.id.feedsBtn);
        championsListView=findViewById(R.id.championsListView);
        coinsView=findViewById(R.id.coinView);


            }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.coinsChampionsBtn:
               coinsChampionsBtn.setBackgroundColor(Color.parseColor("#cc0066"));
               feedsBtn.setBackgroundColor(Color.parseColor("#C0C0C0"));
               commentsListView.setVisibility(View.INVISIBLE);
               championsListView.setVisibility(View.VISIBLE);
                writeCommentView.setVisibility(View.INVISIBLE);
                submitCommentImage.setVisibility(View.INVISIBLE);
               break;

            case R.id.feedsBtn:
                feedsBtn.setBackgroundColor(Color.parseColor("#8ed633"));
                coinsChampionsBtn.setBackgroundColor(Color.parseColor("#C0C0C0"));
                commentsListView.setVisibility(View.VISIBLE);
                championsListView.setVisibility(View.INVISIBLE);
                writeCommentView.setVisibility(View.VISIBLE);
                submitCommentImage.setVisibility(View.VISIBLE);
                break;
        }

    }
}