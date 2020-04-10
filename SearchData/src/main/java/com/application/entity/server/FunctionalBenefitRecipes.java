package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG 功能好处相关的食谱 */
@Data
@Entity(name = "function_benefit_recipe")
public class FunctionalBenefitRecipes {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "function_benefit_num")
  private int funBenefitNum;

  @Column(name = "recipe_num")
  private Long recipeNum;

    public FunctionalBenefitRecipes() {
    }

    public FunctionalBenefitRecipes(int funBenefitNum, Long recipeNum) {
        this.funBenefitNum = funBenefitNum;
        this.recipeNum = recipeNum;
    }
}
