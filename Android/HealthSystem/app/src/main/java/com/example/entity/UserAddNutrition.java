package com.example.entity;

public class UserAddNutrition {
    private Long id;
    private String userAccount;
    private String nutritionName;
    private float maxCount;
    private float minCount;

    public UserAddNutrition() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getNutritionName() {
        return nutritionName;
    }

    public void setNutritionName(String nutritionName) {
        this.nutritionName = nutritionName;
    }

    public float getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    public float getMinCount() {
        return minCount;
    }

    public void setMinCount(float minCount) {
        this.minCount = minCount;
    }
}
