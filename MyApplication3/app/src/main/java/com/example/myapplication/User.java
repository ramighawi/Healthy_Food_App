package com.example.myapplication;

public class User  {


    String userName,userPhone,userID,userImagePath,userEmail;

    public User() {
    }



    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public User(String userName, String userPhone, String userID, String userImagePath, String userEmail) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userID = userID;
        this.userImagePath=userImagePath;
        this.userEmail=userEmail;


    }





}
