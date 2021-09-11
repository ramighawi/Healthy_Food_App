package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView healthyFoodIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hideActionBar();
        healthyFoodIcon= findViewById(R.id.healthFoodIcon);

        healthyFoodIcon.animate().rotation(360).setDuration(3500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
            }
        },5000);

    }
    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }
}