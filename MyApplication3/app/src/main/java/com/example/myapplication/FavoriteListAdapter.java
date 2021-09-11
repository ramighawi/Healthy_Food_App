package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavoriteListAdapter extends ArrayAdapter<Restaurant> {

    ArrayList<Restaurant> restaurantList;
    Context context;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String id = mAuth.getUid();
    Boolean alreadyPressed=false;
    FirebaseDatabase database;
    DatabaseReference myFavRef;




    public FavoriteListAdapter(@NonNull Context context, ArrayList<Restaurant> restaurantList) {
        super(context, R.layout.eachrowinrestaurantslist,restaurantList);


        this.context=context;
        this.restaurantList=restaurantList;



    }


    public View getView(int position, View view, ViewGroup viewGroup) {


        database=FirebaseDatabase.getInstance();
        myFavRef=database.getReference("Users/"+id+"/My Favorite Restaurants");

        //Define an inflater to help me configure how each row in the list view will be shown
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.eachrowinfavoritelist, null, true);

        //Views initialization
        ImageButton imageFavButton = rowView.findViewById(R.id.favBtn);
        ImageView imageViewList = rowView.findViewById(R.id.listImage);
        TextView bigText = rowView.findViewById(R.id.bigText);
        TextView smallText = rowView.findViewById(R.id.smallText);


        Restaurant restaurant = restaurantList.get(position);

        //Set the data into the views from the arraylist provided to the constructor
        bigText.setText(restaurant.getRestaurantName());
        smallText.setText(restaurant.getRestaurantAddress() + ", TEL:" + restaurant.getRestaurantPhone());

        if (restaurant.getRestaurantImagePath()!=null)
        {
            Picasso.get().load(restaurant.getRestaurantImagePath()).into(imageViewList);

        }

        imageFavButton.setImageResource(android.R.drawable.btn_star_big_on);


       imageFavButton.setOnClickListener(new View.OnClickListener() {//Delete a favorite item from list
           @Override
           public void onClick(View v) {
               myFavRef.removeValue();//Removing the value of the previous data reference to push the new value when making a change

               alreadyPressed=false;
               myFavRef.addValueEventListener(new ValueEventListener() {


                   @Override
                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                      context.startActivity(new Intent(getContext(),FavoriteActivity.class));


                       if (alreadyPressed) return;
                       alreadyPressed=true;
                       for (int i = 0; i <restaurantList.size() ; i++) {//Delete an item by comparing the desired item with the whole list,
                           //actually this loop will add the whole items in the list except the wanted item.

                                    Restaurant temp=restaurantList.get(i);
                               if (!(temp.getRestaurantEmail().equals(restaurant.getRestaurantEmail()))&&!(temp.getRestaurantAddress().equals(restaurant.getRestaurantAddress())))
                               {
                                   myFavRef.push().setValue(temp);

                               }

                           }

                       }



                   @Override
                   public void onCancelled(@NonNull @NotNull DatabaseError error) {

                   }
               });
           }
       });
    return rowView;//The form of every row in the list
    }


    }
