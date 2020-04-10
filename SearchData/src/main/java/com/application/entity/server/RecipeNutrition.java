package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Data
public class RecipeNutrition {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long recipe_num;


  @Column(columnDefinition = "LONGTEXT")
  private String nutritions;

    public RecipeNutrition() {
    }

    public RecipeNutrition(Long recipe_num, String nutritions) {
        this.recipe_num = recipe_num;
        this.nutritions = nutritions;
    }
}
