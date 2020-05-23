package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Data
public class HealthDietNutritionStandard {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer healthDietNum;
  private String healthDietName;
  private String heat;
  // 1 体重过低  2  正常   3   超重或肥胖
  private int manKind;
  // 1克脂肪等于9大卡，
  private String fat; // 12  总能量小于30%，2.66   3 小于25% 2.22
  private String protein; // 0.8-1
  // 一克碳水化合物等于4大卡
  private String carbohydrate; // 总能量50-60

  public HealthDietNutritionStandard() {}
}
