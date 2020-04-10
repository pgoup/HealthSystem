package com.example.entity;

public class RecipeNutritionSuggestion {
    private String nutritions;
    private String suggestions;

    public RecipeNutritionSuggestion() {
    }

    public RecipeNutritionSuggestion(String nutritions, String suggestions) {
        this.nutritions = nutritions;
        this.suggestions = suggestions;
    }

    public String getNutritions() {
        return nutritions;
    }

    public void setNutritions(String nutritions) {
        this.nutritions = nutritions;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }
}
