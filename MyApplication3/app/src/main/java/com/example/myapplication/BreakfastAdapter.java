package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class  BreakfastAdapter extends ArrayAdapter<Breakfast> {


    ArrayList <Breakfast> breakfasts;
    Context context;
    FirebaseDatabase database;
    DatabaseReference breakfastRef;
    FirebaseAuth mAuth;
    String currentDate=java.text.DateFormat.getDateInstance().format(new Date());
    Boolean alreadyPressed=false;




    public BreakfastAdapter(@NonNull Context context, ArrayList<Breakfast> breakfasts) {
        super(context, R.layout.eachrowinmydailylist,breakfasts);

        this.breakfasts=breakfasts;
        this.context=context;

    }


    public View getView(int position, View view, ViewGroup viewGroup)

        {
            database=FirebaseDatabase.getInstance();
            mAuth=FirebaseAuth.getInstance();
            String id=mAuth.getUid();

            //Define a database reference
            breakfastRef=database.getReference("Users/"+id+"/"+currentDate+"/Breakfast");


            //Define an inflater to help me configure how each row in the list view will be shown
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View rowView=inflater.inflate(R.layout.eachrowinmydailylist,null, true);


            //Views initialization
            TextView bigText=rowView.findViewById(R.id.bigText);
            TextView smallText=rowView.findViewById(R.id.smallText);
            TextView amountKind=rowView.findViewById(R.id.amountKindTextView);
            ImageButton imageDelBtn=rowView.findViewById(R.id.favBtn);


            //Set the data into the views from the arraylist provided to the constructor
            bigText.setText(breakfasts.get(position).getKindOfFood());
            smallText.setText(breakfasts.get(position).getAmount());
            amountKind.setText(breakfasts.get(position).getKindOfAmount());
            imageDelBtn.setImageResource(android.R.drawable.ic_menu_delete);

            imageDelBtn.setOnClickListener(new View.OnClickListener() {//Delete an item from list
                @Override
                public void onClick(View v) {

                    breakfastRef.removeValue();//Removing the value of the previous data reference to push the new value when making a change
                    alreadyPressed=false;
                    breakfastRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            context.startActivity(new Intent(getContext(),MyDailyListActivity.class));

                            if (alreadyPressed) return;
                            alreadyPressed=true;

                            for (int i = 0; i <breakfasts.size() ; i++) {//Delete an item by comparing the desired item with the whole list,
                                //actually this loop will add the whole items in the list except the wanted item.
                                Breakfast temp=breakfasts.get(i);
                                if (!(temp.getKindOfFood().equals(breakfasts.get(position).getKindOfFood())))
                                {
                                    breakfastRef.push().setValue(temp);
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
