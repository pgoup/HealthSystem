package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Data
public class FoodNutrition {
  @Id
  @Column(name = "food_nutrition_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long foodNum;

  private String foodName;
  private String secondKind;

  @Column(name = "nutrition", columnDefinition = "LONGTEXT")
  private String nutrition;

  public FoodNutrition() {}
}
