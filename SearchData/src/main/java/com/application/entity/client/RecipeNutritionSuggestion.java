package com.application.entity.client;

import lombok.Data;

/** @author PG */
@Data
public class RecipeNutritionSuggestion {
  private String nutritions;
  private String suggestions;

  public RecipeNutritionSuggestion(String nutritions, String suggestions) {
    this.nutritions = nutritions;
    this.suggestions = suggestions;
  }
}
