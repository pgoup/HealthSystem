package com.example.entity;

public class FoodItemClient {
    private String foodName;
    private byte[] picPath;

    public FoodItemClient() {
    }

    public FoodItemClient(String foodName, byte[] picPath) {
        this.foodName = foodName;
        this.picPath = picPath;
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
}
