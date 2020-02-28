package com.application.service;

import com.application.entity.client.FoodClient;
import com.application.entity.client.FoodItemClient;

import java.util.List;

public interface FoodService {
    /**
     * 获取所有的一级食物分类
     */
    List<String> getAllFirstKindFood();

    /**
     * 根据一级分类获取二级分类
     */
    List<String> getSecondKindByFirstKind(String firstKindFood);

    /**
     * 根据二级分类获取相应的食物
     */
    List<FoodItemClient> getFoodsBySecondKind(String secondKind);

    /**
     * 根据名称获取具体的食物信息
     */
    FoodClient getFoodByName(String name);
}
