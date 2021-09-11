package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class AddingRestaurantsActivity extends AppCompatActivity {

    final static int GET_IMAGE_FROM_GALLERY=1;
    FloatingActionButton addBtn;
    EditText restaurantName,restaurantAddress,restaurantEmail,restaurantPhone;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Uri imageUri;
    StorageReference storageReference;
    String url;
    ImageView restaurantImageView,uploadImageView;
    Activity currentActivity;
    boolean alreadyPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_restaurants);


        initViews();
        initGeneral();
        hideActionBar();
        onClickListeners();


    }

    private void onClickListeners() {

        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getImageFromGallery();

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if  (TextUtils.isEmpty(restaurantName.getText().toString())) {//Ensure that field is not empty
                    Toast.makeText(getApplicationContext(), "Please Insert Restaurant Name!", Toast.LENGTH_LONG).show();
                    return;
                }
                if  (TextUtils.isEmpty(restaurantAddress.getText().toString())) {//Ensure that field is not empty
                    Toast.makeText(getApplicationContext(), "Please Insert Restaurant Address!", Toast.LENGTH_LONG).show();
                    return;
                }


                if (imageUri==null)//Ensure that image was selected (the string var includes image data)
                {
                    Toast.makeText(getApplicationContext(),"Please Select an Image",Toast.LENGTH_LONG).show();

                    return;
                }

                uploadImage();//Upload image to the fireStore

            }


        });
    }

    private void initGeneral() {
        alreadyPressed=false;
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference("Restaurants");
        currentActivity=AddingRestaurantsActivity.this;
    }

    private void initViews() {
        restaurantName = findViewById(R.id.restaurantName);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        restaurantEmail = findViewById(R.id.restaurantEmail);
        restaurantPhone = findViewById(R.id.restaurantPhone);
        restaurantImageView=findViewById(R.id.restaurantImageView);
        addBtn=findViewById(R.id.addBtn);
        uploadImageView=findViewById(R.id.uploadImageView);
    }

    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }

    private void getImageFromGallery() {
                   Intent intent = new Intent();
                   intent.setType("image/*");
                   intent.setAction(Intent.ACTION_GET_CONTENT);
                   startActivityForResult(intent,GET_IMAGE_FROM_GALLERY);


        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if  (requestCode==GET_IMAGE_FROM_GALLERY){

                if (resultCode==RESULT_OK){

                    imageUri=data.getData();//saving the image data in a string var
                    Picasso.get().load(imageUri).into(restaurantImageView);//set the image in the current activity
                }


        }

    }

    private void uploadImage() {

        if (imageUri !=null){
            //build a reference called "Restaurants" include the images named with the name of the restaurant
            storageReference= FirebaseStorage.getInstance().getReference().child("Restaurants").child(restaurantName.getText().toString()+"."+getFileExtension(imageUri));


            //Insert the image into the reference, download the image to be added to the firebase data as a url
            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url=uri.toString();
                            addDataToDB();
                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri imageUri) {//Function to get the extension of the image as a string
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void addDataToDB() {//Add a new restaurant
        alreadyPressed=false;
           
            Restaurant restaurant=new Restaurant(restaurantName.getText().toString(),restaurantAddress.getText().toString(),restaurantEmail.getText().toString(),restaurantPhone.getText().toString(),url);


            myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (alreadyPressed) return;
                alreadyPressed=true;
                for (DataSnapshot db: snapshot.getChildren()) {//Ensure that the restaurant does not exist by comparing the email and the
                    // address of the new restaurant with the other restaurants in the database reference

                    Restaurant temp=db.getValue(Restaurant.class);

                    if (temp.getRestaurantAddress().equals(restaurant.getRestaurantAddress())&&temp.getRestaurantEmail().equals(restaurant.getRestaurantEmail()))
                    {
                        Toast.makeText(getApplicationContext(), "Restaurant Exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                myRef.push().setValue(restaurant);//Adding the new restaurant
                Toast.makeText(getApplicationContext(), "Has Added Successfully", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(currentActivity,HealthyRestaurantsActivity.class));


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        

    }


}