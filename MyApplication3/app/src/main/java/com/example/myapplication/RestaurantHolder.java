package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    ImageButton imageFavButton;
    ImageView imageViewList;
    TextView bigText;

    Context context;
    DatabaseReference myFavRef,myRestaurantRef;
    FirebaseDatabase database;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String id = mAuth.getUid();
    Boolean alreadyPressed=false;


    public RestaurantHolder(Context context, View itemView) {
        super(itemView);
        this.context=context;
        this.imageFavButton= (ImageButton) itemView.findViewById(R.id.favBtn);
        this.imageViewList=(ImageView) itemView.findViewById(R.id.listImage);
        this.bigText=(TextView) itemView.findViewById(R.id.bigText);


        itemView.setOnClickListener(this);


    }
    public void bindRestaurant(Restaurant restaurant) {

        //Bind the data to the ViewHolder
        database=FirebaseDatabase.getInstance();

        myFavRef=database.getReference("Users/" + id + "/My Favorite Restaurants");
        myRestaurantRef=database.getReference("Restaurants");

        bigText.setText(restaurant.getRestaurantName());

        if (restaurant.getRestaurantImagePath()!=null)
        {
            Picasso.get().load(restaurant.getRestaurantImagePath()).into(imageViewList);

        }



        myFavRef.addValueEventListener(new ValueEventListener() {//Set the favorite button value as ON if it exists in the favorite list by checking
            //the favorite reference
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dp: snapshot.getChildren()
                ) {
                    Restaurant temp=dp.getValue(Restaurant.class);

                    if ((restaurant.getRestaurantAddress().equals(temp.getRestaurantAddress()))&&(restaurant.getRestaurantEmail().equals(temp.getRestaurantEmail())))
                    {
                        imageFavButton.setImageResource(android.R.drawable.btn_star_big_on);
                        return;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        imageFavButton.setImageResource(android.R.drawable.btn_star_big_off);//Default status for the favorite button is off

        imageFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alreadyPressed=false;

                myFavRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                        if (alreadyPressed) return;
                        alreadyPressed=true;

                        for (DataSnapshot dp: snapshot.getChildren()) {//To ensure there is no duplicates
                            Restaurant temp=dp.getValue(Restaurant.class);

                            if (((temp.getRestaurantEmail().equals(restaurant.getRestaurantEmail())) && (temp.getRestaurantAddress().equals(restaurant.getRestaurantAddress()))))
                            {


                                Toast.makeText(context.getApplicationContext(), "Restaurant Exist", Toast.LENGTH_LONG).show();
                                return;
                            }

                        }

                        myFavRef.push().setValue(restaurant);
                        Toast.makeText(context.getApplicationContext(), "Has Added Successfully!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

        });




    }
    public ImageView getImageViewList() {
        return imageViewList;
    }

    public TextView getBigText() {
        return bigText;
    }



    @Override
    public void onClick(View v) {


    }
}
