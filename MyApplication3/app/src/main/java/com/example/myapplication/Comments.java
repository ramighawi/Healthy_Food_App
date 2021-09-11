package com.example.myapplication;

public class Comments {

    String commentMakerName,commentMakerUID,commentDateTime,commentText;

    public Comments()
    {

    }

    public Comments(String commentMakerName, String commentMakerUID, String commentDateTime, String commentText) {
        this.commentMakerName = commentMakerName;
        this.commentMakerUID = commentMakerUID;
        this.commentDateTime = commentDateTime;
        this.commentText = commentText;
    }

    public String getCommentMakerName() {
        return commentMakerName;
    }

    public void setCommentMakerName(String commentMakerName) {
        this.commentMakerName = commentMakerName;
    }

    public String getCommentMakerUID() {
        return commentMakerUID;
    }

    public void setCommentMakerUID(String commentMakerUID) {
        this.commentMakerUID = commentMakerUID;
    }

    public String getCommentDateTime() {
        return commentDateTime;
    }

    public void setCommentDateTime(String commentDateTime) {
        this.commentDateTime = commentDateTime;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
