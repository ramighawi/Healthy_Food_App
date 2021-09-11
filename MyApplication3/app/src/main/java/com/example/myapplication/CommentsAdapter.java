package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsAdapter extends ArrayAdapter<Comments> {

    ArrayList<Comments> commentsArrayList;
    Context context;
    FirebaseDatabase database;
    DatabaseReference usersRef;

    public CommentsAdapter(@NonNull Context context, ArrayList<Comments> commentsArrayList) {
        super(context,R.layout.eachrowincommentslist, commentsArrayList);

        this.context=context;
        this.commentsArrayList=commentsArrayList;

    }



    public View getView(int position, View view, ViewGroup viewGroup){

        database=FirebaseDatabase.getInstance();
        usersRef=database.getReference("Users");  //Define a database reference

        //Define an inflater to help me configure how each row in the list view will be shown
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.eachrowincommentslist,null,true);

        //Views initialization
        ImageView profileView=rowView.findViewById(R.id.profileView);
        TextView userNameView=rowView.findViewById(R.id.userNameView);
        TextView dateTimeView=rowView.findViewById(R.id.dateTimeView);
        TextView commentView=rowView.findViewById(R.id.commentView);

        Comments comment=commentsArrayList.get(position);

        //Set the data into the views from the arraylist provided to the constructor
        userNameView.setText(comment.commentMakerName);
        commentView.setText(comment.commentText);
        dateTimeView.setText(comment.commentDateTime);



        //Set the profile image of the champion
        usersRef.child(comment.commentMakerUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if (user!=null){
                if (user.userImagePath.equals(""))
                {

                    profileView.setImageResource(R.drawable.profile_pic);//Set a default image
                }

                else
                Picasso.get().load(user.getUserImagePath()).into(profileView);}//load the image into the view by Picasso

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return rowView;//The form of every row in the list
    }
}
