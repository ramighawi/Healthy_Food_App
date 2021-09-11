package com.example.myapplication;

public class Champion {
    String championName, championImagePath,championCoins,championEmail;





    public Champion(){}

    public Champion(String championName, String championImagePath, String championCoins,String championEmail) {
        this.championName = championName;
        this.championImagePath = championImagePath;
        this.championCoins = championCoins;
        this.championEmail=championEmail;
    }

    public String getChampionEmail() {
        return championEmail;
    }

    public void setChampionEmail(String championEmail) {
        this.championEmail = championEmail;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public String getChampionImagePath() {
        return championImagePath;
    }

    public void setChampionImagePath(String championImagePath) {
        this.championImagePath = championImagePath;
    }

    public String getChampionCoins() {
        return championCoins;
    }

    public void setChampionCoins(String championCoins) {
        this.championCoins = championCoins;
    }
}
