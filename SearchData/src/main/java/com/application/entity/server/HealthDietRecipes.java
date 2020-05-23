package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG 营养饮食和食谱的关系实体类 */
@Data
@Entity(name = "health_diet_recipe")
public class HealthDietRecipes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "recipe_num")
  private Long recipeNum;

  @Column(name = "health_diet_num")
  private int healthDietNum;

  public HealthDietRecipes() {}

  public HealthDietRecipes(Long recipeNum, int healthDietNum) {
    this.recipeNum = recipeNum;
    this.healthDietNum = healthDietNum;
  }
}
