package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChampionAdapter extends ArrayAdapter<Champion> {

    ArrayList<Champion> championArrayList;
    Context context;

    public ChampionAdapter(@NonNull Context context, ArrayList<Champion>championArrayList) {
        super(context, R.layout.eachrowinchampionslist,championArrayList);

        this.championArrayList=championArrayList;
        this.context=context;
    }

    public View getView(int position, View view, ViewGroup viewGroup){

        //Define an inflater to help me configure how each row in the list view will be shown
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.eachrowinchampionslist,null,true);

        //Views initialization
        ImageView profileView=rowView.findViewById(R.id.profileView);
        TextView userNameView=rowView.findViewById(R.id.userNameView);
        TextView sumOfCoinsView=rowView.findViewById(R.id.sumOfCoinsView);
        TextView championEmailView=rowView.findViewById(R.id.championEmailView);

        Champion champion=championArrayList.get(position);

        //Set the data into the views from the arraylist provided to the constructor
        userNameView.setText(champion.championName);
        sumOfCoinsView.setText(champion.championCoins);
        championEmailView.setText(champion.championEmail);


        //Set the profile image of the champion
        if (champion.getChampionImagePath().equals(""))
            profileView.setImageResource(R.drawable.profile_pic);//Set a default image
        else
            Picasso.get().load(champion.getChampionImagePath()).into(profileView);//load the image into the view by Picasso

        return rowView;//The form of every row in the list
    }
}
