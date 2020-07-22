package com.example.entity;

public class FoodClient {
    private String foodName;
    private byte[] picPath;
    private String healthWorth;
    private String introduction;
    private String benefit;
    private String suitablePeople;
    private String tabooPeople;
    private String nutritionalIngredient;

    public FoodClient() {
    }

    public FoodClient(String foodName, byte[] picPath, String healthWorth, String introduction, String benefit, String suitablePeople, String tabooPeople, String nutritionalIngredient) {
        this.foodName = foodName;
        this.picPath = picPath;
        this.healthWorth = healthWorth;
        this.introduction = introduction;
        this.benefit = benefit;
        this.suitablePeople = suitablePeople;
        this.tabooPeople = tabooPeople;
        this.nutritionalIngredient = nutritionalIngredient;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public byte[] getPicPath() {
        return picPath;
    }

    public void setPicPath(byte[] picPath) {
        this.picPath = picPath;
    }

    public String getHealthWorth() {
        return healthWorth;
    }

    public void setHealthWorth(String healthWorth) {
        this.healthWorth = healthWorth;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getSuitablePeople() {
        return suitablePeople;
    }

    public void setSuitablePeople(String suitablePeople) {
        this.suitablePeople = suitablePeople;
    }

    public String getTabooPeople() {
        return tabooPeople;
    }

    public void setTabooPeople(String tabooPeople) {
        this.tabooPeople = tabooPeople;
    }

    public String getNutritionalIngredient() {
        return nutritionalIngredient;
    }

    public void setNutritionalIngredient(String nutritionalIngredient) {
        this.nutritionalIngredient = nutritionalIngredient;
    }
}
