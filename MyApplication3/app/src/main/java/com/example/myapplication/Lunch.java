package com.example.myapplication;

public class Lunch {

    String kindOfFood,amount,kindOfAmount;

    public Lunch(String kindOfFood, String amount,String kindOfAmount) {
        this.kindOfFood = kindOfFood;
        this.amount = amount;
        this.kindOfAmount=kindOfAmount;
    }

    public Lunch() {
    }

    public String getKindOfAmount() {
        return kindOfAmount;
    }

    public void setKindOfAmount(String kindOfAmount) {
        this.kindOfAmount = kindOfAmount;
    }

    public String getKindOfFood() {
        return kindOfFood;
    }

    public void setKindOfFood(String kindOfFood) {
        this.kindOfFood = kindOfFood;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
