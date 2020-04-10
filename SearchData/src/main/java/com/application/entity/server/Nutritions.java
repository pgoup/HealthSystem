package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Data
public class Nutritions {
  @Id
  @Column(name = "food_nutrition_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long foodNum;

  // 热量
  private float heat;

  // 胆固醇
  private float cholesterol;
  // 蛋白质
  private float protein;
  // 碳水化合物
  private float carbohydrate;
  // 脂肪
  private float fat;
  // 维生素A-E
  private float vitaminA;
  private float vitaminB;
  private float vitaminB1;
  private float vitaminB2;
  private float vitaminB6;
  private float vitaminB12;
  private float vitaminC;
  private float vitaminD;
  private float vitaminE;
  // 钾钙钠镁磷铁锌铜锰碘
  private float Ka;
  private float Ca;
  private float Na;
  private float Mg;
  private float P;
  private float Fe;
  private float Zn;
  private float Cu;
  private float Mn;
  private float I;

  public Nutritions() {}
}
