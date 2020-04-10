package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/**
 * @author PG
 */

@Entity
@Data
public class Food {
    @Id
    @Column(name = "food_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long number;

    @Column(name = "food_name", unique = true)
    private String foodName;

    @Column(name = "pic_path")
    private String picPath;

    /**
     * 一级分类
     */
    @Column(name = "first_kind")
    private String firstKind;
    /**
     * 二级分类
     */
    @Column(name = "second_kind")
    private String secondKind;

    /**
     * 营养价值
     */
    @Column(name = "health_worth", columnDefinition = "LONGTEXT")
    private String healthWorth;

    /**
     * 食材简介
     */
    @Column(columnDefinition = "LONGTEXT")
    private String introduction;

    /**
     * 食用功效
     */
    @Column(columnDefinition = "LONGTEXT")
    private String benefit;

    /**
     * 适用人群
     */
    @Column(name = "suitable_people")
    private String suitablePeople;

    /**
     * 禁忌人群
     */
    @Column(name = "taboo_people")
    private String tabooPeople;

    /**
     * 营养成分
     */
    @Column(name = "nutritional_ingredient", columnDefinition = "LONGTEXT")
    private String nutritionalIngredient;

    public Food() {
    }

    public Food(Long number, String foodName, String picPath, String firstKind, String secondKind, String healthWorth, String introduction, String benefit, String suitablePeople, String tabooPeople, String nutritionalIngredient) {
        this.number = number;
        this.foodName = foodName;
        this.picPath = picPath;
        this.firstKind = firstKind;
        this.secondKind = secondKind;
        this.healthWorth = healthWorth;
        this.introduction = introduction;
        this.benefit = benefit;
        this.suitablePeople = suitablePeople;
        this.tabooPeople = tabooPeople;
        this.nutritionalIngredient = nutritionalIngredient;
    }
}

