package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity  {
    private static final int PERMISSION_CAMERA = 222;
    private static final int CAPTURE_IMAGE = 2;
    final static int GET_IMAGE_FROM_GALLERY=1;
    ImageView profileImage,takeImageBtn,imageFromGalleryBtn,logoImageView,savePhoneImageView,editPhoneImageView,saveNameImageView,editNameImageView;
    BoomMenuButton bmb,bmb2;
    Bitmap photo;
    String userImagePath;
    Uri imageUri;
    StorageReference storageReference;
    TextView nameTextView,sumOfCoinsView,idTextView,phoneTextView,emailTextView;
    EditText editTextPhone,editTextName;

    FirebaseAuth mAuth;
    String id;

    FirebaseDatabase database;
    DatabaseReference databaseRef;
    Activity currentActivity;
    boolean alreadyPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        initViews();
        initGeneral();
        userNameAndProfileImageView();
        emailAndPhoneAndIDViews();
        menuMaker();
        hideActionBar();
        sumOfCoinsViewSetValue();
        viewsOnclickListeners();



    }

    private void updatePhoneNumber() {//Updating the phone number using a map and "updateChildren"
        alreadyPressed = false;
        databaseRef.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map map=new HashMap();
                if (alreadyPressed) return;
                alreadyPressed=true;

                map.put("userPhone",editTextPhone.getText().toString());
                editTextPhone.setVisibility(View.INVISIBLE);//After updating make the edit text invisible
                savePhoneImageView.setVisibility(View.INVISIBLE);//After updating the image view invisible
                editTextPhone.setText("");
                databaseRef.child("Users").child(id).updateChildren(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void emailAndPhoneAndIDViews() {
        databaseRef.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                emailTextView.setText(user.userEmail);
                phoneTextView.setText(user.userPhone);
                idTextView.setText(user.userID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void viewsOnclickListeners() {
        editNameImageView.setOnClickListener(new View.OnClickListener() {//When click, the edit name view ,
            // the edit text and the save image will be visible
            @Override
            public void onClick(View v) {
                editTextName.setVisibility(View.VISIBLE);
                saveNameImageView.setVisibility(View.VISIBLE);
                saveNameImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//To ensure that the field is not empty
                        if (TextUtils.isEmpty(editTextName.getText().toString())){
                            Toast.makeText(currentActivity, "Please Fill Your Name",
                                    Toast.LENGTH_SHORT).show();
                            return;}
                        updateName();
                    }
                });
            }
        });
        editPhoneImageView.setOnClickListener(new View.OnClickListener() {//When click, the edit name view ,
            // the edit text and the save image will be visible
            @Override
            public void onClick(View v) {
                editTextPhone.setVisibility(View.VISIBLE);
                savePhoneImageView.setVisibility(View.VISIBLE);
                savePhoneImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//To ensure that the field is not empty
                        if (TextUtils.isEmpty(editTextPhone.getText().toString())){
                            Toast.makeText(currentActivity, "Please Fill Your Phone",
                                    Toast.LENGTH_SHORT).show();
                            return;}
                        updatePhoneNumber();
                    }
                });
            }
        });
        takeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeImage();

            }

        });

        imageFromGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();

            }
        });
        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(currentActivity,WelcomeActivity.class));
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
            }
        });
    }
    private void updateName() {//update name using a map and updateChildren
        alreadyPressed=false;
        databaseRef.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map map=new HashMap();
                if (alreadyPressed)
                    return;
                alreadyPressed=true;

                map.put("userName",editTextName.getText().toString());
                editTextName.setVisibility(View.INVISIBLE);//After updating make the edit text invisible
                editTextName.setText("");
                saveNameImageView.setVisibility(View.INVISIBLE);//After updating make the edit text invisible
                databaseRef.child("Users").child(id).updateChildren(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void userNameAndProfileImageView() {
        databaseRef.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user!=null){
                    nameTextView.setText(user.getUserName());
                    if (user.userImagePath.equals(""))//If there is no image set a default image
                        profileImage.setImageResource(R.drawable.profile_pic);
                    else
                        Picasso.get().load(user.getUserImagePath()).into(profileImage);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initGeneral() {
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getUid();
        storageReference= FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        currentActivity=ProfileActivity.this;
        databaseRef=database.getReference();
    }
    private void initViews() {
        profileImage=findViewById(R.id.profileImage);
        nameTextView=findViewById(R.id.nameTextView);
        sumOfCoinsView=findViewById(R.id.sumOfCoinsView);
        bmb=findViewById(R.id.boomBtn3);
        bmb2=findViewById(R.id.boomBtn2);
        takeImageBtn=findViewById(R.id.takeImageBtn);
        imageFromGalleryBtn=findViewById(R.id.imageFromGalleryBtn);
        logoImageView=findViewById(R.id.logoImageView);
        idTextView=findViewById(R.id.idTextView);
        phoneTextView=findViewById(R.id.phoneTextView);
        emailTextView=findViewById(R.id.emailTextView);
        editTextPhone=findViewById(R.id.editTextPhone);
        savePhoneImageView=findViewById(R.id.savePhoneImageView);
        editPhoneImageView=findViewById(R.id.editPhoneImageView);
        editNameImageView=findViewById(R.id.editNameImageView);
        saveNameImageView=findViewById(R.id.saveNameImageView);
        editTextName=findViewById(R.id.editTextName);
    }
    private void menuMaker() {

        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb2.setButtonEnum(ButtonEnum.TextOutsideCircle);
        String titles[] = { "Home", "Profile", "About", "Logout"};//Define the titles of the right menu
        String titles2[]={"My Menu","Healthy Restaurants",  "Favorite Restaurants", "Food List"};//Define the titles of the left menu
        int icon[] = {R.mipmap.about_icon,R.mipmap.profile_icon_foreground,R.mipmap.homepage_icon_foreground,R.mipmap.logout};//Define
        //the icons of the right menu
        int icon2[]={ R.mipmap.menu_food_icon,R.mipmap.healthy_icon,R.mipmap.favorite_icon, R.mipmap.foodlist_icon};//Define the icons
        //of the left menu
        int colors[]={R.color.green,R.color.purple_1000,R.color.purple_1000,R.color.green};//Define the text color of the titles of the menu



        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {//Build the right menu including: about , profile,
            // welcome(homepage) activities + logout
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.

                            switch (index){
                                case 0:
                                    startActivity(new Intent(currentActivity, AboutActivity.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(currentActivity, ProfileActivity.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(currentActivity, WelcomeActivity.class));
                                    break;
                                case 3:
                                    logoutUser();

                            }



                        }
                    }).normalImageRes(icon[i]).normalText(titles[i]).normalColor(Color.WHITE);


            bmb.addBuilder(builder);

        }
        for (int i = 0; i < bmb2.getPiecePlaceEnum().pieceNumber(); i++) {//Build the left menu including: my menu (my daily list), healthy restaurants,
            //favorite and food list activities
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.

                            switch (index){
                                case 0:
                                    startActivity(new Intent(currentActivity, MyDailyListActivity.class));
                                    break;
                                case 1:

                                    startActivity(new Intent(currentActivity, HealthyRestaurantsActivity.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(currentActivity, FavoriteActivity.class));
                                    break;
                                case 3:
                                    startActivity(new Intent(currentActivity, FoodListActivity.class));

                                    break;
                            }



                        }
                    }).normalImageRes(icon2[i]).normalText(titles2[i]).normalColor(Color.WHITE).normalTextColorRes(colors[i]).typeface(Typeface.DEFAULT_BOLD).rotateText(false).textWidth(280);
            bmb2.addBuilder(builder);

        }


    }//Boom menu maker (basic menu by external library)
    private void logoutUser() {

        new AlertDialog.Builder(currentActivity)
                .setTitle("Logout")
                .setMessage("Are You Sure You Want To Logout")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(currentActivity,MainActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }
    private void uploadImageFromGallery() {
        if (imageUri !=null){
            //build a storage reference called "profileImages" include the images named with the UID of the user
            storageReference= FirebaseStorage.getInstance().getReference().child("profileImages").child(id);

            //Insert the image into the reference
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this,"Failed",Toast.LENGTH_LONG).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {// download the image and add it to the firebase data as a url
                            userImagePath=uri.toString();
                            databaseRef.child("Users").child(id).child("userImagePath").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    databaseRef.child("Users").child(id).child("userImagePath").setValue(userImagePath);
                                    Picasso.get().load(userImagePath).into(profileImage);
                                    finish();
                                    startActivity(getIntent());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            });
        }
    }
    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GET_IMAGE_FROM_GALLERY);
    }
    private void sumOfCoinsViewSetValue() {
        databaseRef.child("Users").child(id).child("Coins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren()) {

                    Map<String, String> value = (Map<String, String>) snapshot.getValue();

                    sumOfCoinsView.setText(value.values().toArray()[0].toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void hideActionBar() {
        // Define ActionBar object
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

    }
    private void takeImage() {
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISSION_CAMERA);

        }
        else {
            Intent cam = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cam, CAPTURE_IMAGE);

        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CAPTURE_IMAGE://When the image is token by camera
                if  ( resultCode == RESULT_OK) {
                    ProgressDialog.show(this, "Loading", "Wait while loading...");

                    photo = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageUri=data.getData();

                    byte[] b = stream.toByteArray();
                    StorageReference storageReference =FirebaseStorage.getInstance().getReference().child("profileImages").child(id);

                    storageReference.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {//Upload the image and save
                        //it in a storage reference named with user UID
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Toast.makeText(currentActivity, "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@android.support.annotation.NonNull Exception e) {
                            Toast.makeText(currentActivity,"Failed",Toast.LENGTH_LONG).show();


                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) { //download the image and add it to the firebase data as a url
                                    userImagePath=uri.toString();
                                    databaseRef.child("Users").child(id).child("userImagePath").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            databaseRef.child("Users").child(id).child("userImagePath").setValue(userImagePath);
                                            Picasso.get().load(userImagePath).into(profileImage);//Set the profile image
                                            finish();
                                            startActivity(getIntent());

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                break;
            case GET_IMAGE_FROM_GALLERY://when the image is token from the gallery
                if  ( resultCode == RESULT_OK){
                    ProgressDialog.show(this, "Loading", "Wait while loading...");
                imageUri=data.getData();
                uploadImageFromGallery();}
                break;
        }



    }


     //permission to use the camera
    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull @org.jetbrains.annotations.NotNull String[] permissions, @androidx.annotation.NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==PERMISSION_CAMERA){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_LONG).show();

                takeImage();
            }
            else
            {    Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();

            }
        }
    }

}