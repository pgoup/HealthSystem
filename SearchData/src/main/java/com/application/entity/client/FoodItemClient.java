package com.application.entity.client;

import lombok.Data;

/**
 * @author PG
 */
@Data
public class FoodItemClient {
    private String foodName;
    private byte[] picPath;

    public FoodItemClient() {
    }

    public FoodItemClient(String foodName, byte[] picPath) {
        this.foodName = foodName;
        this.picPath = picPath;
    }
}
