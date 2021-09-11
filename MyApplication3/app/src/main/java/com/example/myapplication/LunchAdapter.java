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

public class LunchAdapter extends ArrayAdapter<Lunch> {

    ArrayList<Lunch> lunches;
    Context context;
    FirebaseDatabase database;
    DatabaseReference lunchRef;
    FirebaseAuth mAuth;
    String currentDate=java.text.DateFormat.getDateInstance().format(new Date());
    Boolean alreadyPressed=false;



          public LunchAdapter(@NonNull Context context, ArrayList<Lunch> lunches) {
               super(context,R.layout.eachrowinmydailylist,lunches);

               this.context=context;
               this.lunches=lunches;

          }


         public View getView(int position, View view, ViewGroup viewGroup){

             database=FirebaseDatabase.getInstance();
             mAuth=FirebaseAuth.getInstance();
             String id=mAuth.getUid();

             //Define a database reference
             lunchRef=database.getReference("Users/"+id+"/"+currentDate+"/Lunch");

             //Define an inflater to help me configure how each row in the list view will be shown
             LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
             View rowView=inflater.inflate(R.layout.eachrowinmydailylist,null,true);

             //Views initialization
             TextView bigText=rowView.findViewById(R.id.bigText);
             TextView smallText=rowView.findViewById(R.id.smallText);
             TextView amountKind=rowView.findViewById(R.id.amountKindTextView);
             ImageButton imageDelBtn=rowView.findViewById(R.id.favBtn);

             //Set the data into the views from the arraylist provided to the constructor
             bigText.setText(lunches.get(position).getKindOfFood());
             smallText.setText(lunches.get(position).getAmount());
             amountKind.setText(lunches.get(position).getKindOfAmount());

             imageDelBtn.setImageResource(android.R.drawable.ic_menu_delete);

             imageDelBtn.setOnClickListener(new View.OnClickListener() {//Delete an item from list
                 @Override
                 public void onClick(View v) {

                     lunchRef.removeValue();//Removing the value of the previous data reference to push the new value when making a change

                     alreadyPressed=false;
                     lunchRef.addValueEventListener(new ValueEventListener() {


                         @Override
                         public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                             context.startActivity(new Intent(getContext(),MyDailyListActivity.class));

                             if (alreadyPressed) return;
                             alreadyPressed=true;

                             for (int i = 0; i <lunches.size() ; i++) {//Delete an item by comparing the desired item with the whole list,
                                 //actually this loop will add the whole items in the list except the wanted item.

                                 Lunch temp=lunches.get(i);
                                 if (!(temp.getKindOfFood().equals(lunches.get(position).getKindOfFood())))
                                 {
                                     lunchRef.push().setValue(temp);

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
