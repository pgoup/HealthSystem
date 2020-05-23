package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity(name = "user_add_nutrition")
@Data
public class UserAddNutrition {
  @Id
  @Column(name = "add_nutrition_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userAccount;
  private String nutritionName;
  private float maxCount;
  private float minCount;

  public UserAddNutrition() {}
}
