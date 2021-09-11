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

public class DinnerAdapter extends ArrayAdapter<Dinner> {

    ArrayList<Dinner> dinners;
    Context context;
    FirebaseDatabase database;
    DatabaseReference dinnerRef;
    FirebaseAuth mAuth;
    String currentDate=java.text.DateFormat.getDateInstance().format(new Date());
    Boolean alreadyPressed=false;



    public DinnerAdapter(@NonNull Context context, ArrayList<Dinner> dinners) {
        super(context, R.layout.eachrowinmydailylist,dinners);

        this.context=context;
        this.dinners=dinners;
    }

    public View getView(int position, View view, ViewGroup viewGroup){
        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String id=mAuth.getUid();

       //Define a database reference
        dinnerRef=database.getReference("Users/"+id+"/"+currentDate+"/Dinner");

        //Define an inflater to help me configure how each row in the list view will be shown
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.eachrowinmydailylist,null,true);

        //Views initialization
        TextView bigText=rowView.findViewById(R.id.bigText);
        TextView smallText=rowView.findViewById(R.id.smallText);
        TextView amountKind=rowView.findViewById(R.id.amountKindTextView);
        ImageButton imageDelBtn=rowView.findViewById(R.id.favBtn);

        //Set the data into the views from the arraylist provided to the constructor
        bigText.setText(dinners.get(position).kindOfFood);
        smallText.setText(dinners.get(position).amount);
        amountKind.setText(dinners.get(position).kindOfAmount);
        imageDelBtn.setImageResource(android.R.drawable.ic_menu_delete);

        imageDelBtn.setOnClickListener(new View.OnClickListener() {//Delete an item from list
            @Override
            public void onClick(View v) {

                dinnerRef.removeValue();//Removing the value of the previous data reference to push the new value when making a change

                alreadyPressed=false;
                dinnerRef.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        context.startActivity(new Intent(getContext(),MyDailyListActivity.class));


                        if (alreadyPressed) return;
                        alreadyPressed=true;
                        for (int i = 0; i <dinners.size() ; i++) {//Delete an item by comparing the desired item with the whole list,
                            //actually this loop will add the whole items in the list except the wanted item.

                            Dinner temp=dinners.get(i);
                            if (!(temp.getKindOfFood().equals(dinners.get(position).getKindOfFood())))
                            {
                                dinnerRef.push().setValue(temp);

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
