package com.example.myapplication;

public class Breakfast {

    String kindOfFood,Amount,kindOfAmount;


    public Breakfast(String kindOfFood, String amount,String kindOfAmount) {
        this.kindOfFood = kindOfFood;
        this.Amount = amount;
        this.kindOfAmount=kindOfAmount;
    }

    public Breakfast() {
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
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
