package com.application.entity.client;

import lombok.Data;

/** @author PG */
@Data
public class RecipeNutritionClient {
  private String recipeName;
  private byte[] recipeImage;
  private String nutritions;

  public RecipeNutritionClient() {}
}
