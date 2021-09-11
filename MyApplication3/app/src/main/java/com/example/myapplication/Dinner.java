package com.example.myapplication;

public class Dinner {
    String kindOfFood, amount,kindOfAmount;

    public Dinner(String kindOfFood, String amount,String kindOfAmount) {
        this.kindOfFood = kindOfFood;
        this.amount = amount;
        this.kindOfAmount=kindOfAmount;
    }

    public Dinner() {
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
