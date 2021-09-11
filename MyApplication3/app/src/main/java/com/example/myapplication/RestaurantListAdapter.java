package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantHolder> {

    ArrayList<Restaurant> restaurantList;
    Context context;

    OnImageClickListener onImageClickListener;




    public RestaurantListAdapter(@NonNull Context context, ArrayList<Restaurant> restaurantList) {


        this.restaurantList = restaurantList;
        this.context = context;

        try {
            this.onImageClickListener = ((OnImageClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnImageClickListener.");
        }

    }



    @NonNull
    @NotNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eachrowinrestaurantslist,parent, false);
        return new RestaurantHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantHolder holder, int position) {

        //  Use position to access the correct Restaurant object
        Restaurant restaurant = this.restaurantList.get(position);

        // Bind the restaurant object to the holder
        holder.bindRestaurant(restaurant);

        holder.imageViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onImageClickListener.onImageClick(restaurant.getRestaurantImagePath(), restaurant.getRestaurantName(), restaurant.getRestaurantAddress()+", Tel:"+restaurant.getRestaurantPhone());
            }
        });







    }

    @Override
    public int getItemCount() {
        return this.restaurantList.size();
    }

}



