package com.application.entity.client;


import lombok.Data;

/**
 * @author PG
 */
@Data
public class FoodClient {
    private String foodName;
    private byte[] picPath;
    /**
     * 营养价值
     */
    private String healthWorth;

    /**
     * 食材简介
     */
    private String introduction;

    /**
     * 食用功效
     */
    private String benefit;

    /**
     * 适用人群
     */
    private String suitablePeople;

    /**
     * 禁忌人群
     */
    private String tabooPeople;

    /**
     * 营养成分
     */
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
}
