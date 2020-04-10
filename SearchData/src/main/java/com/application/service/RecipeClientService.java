package com.application.service;

import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.RecipeClient;
import com.application.entity.client.RecipeNutritionClient;
import com.application.entity.server.Recipe;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RecipeClientService {

  List<RecipeItemClient> getRecipesByRecipeKind(String recipeKind, Pageable pageable);

  List<RecipeItemClient> getRecipesByHealthDiet(String healthDiet, Pageable pageable);

  List<RecipeItemClient> getRecipesByFunctionBenefit(String functionBenefit, Pageable pageable);

  RecipeClient getRecipeByNum(String recipeNum, Long userAccount);

  List<RecipeItemClient> getRecipesByKeyWord(String keyWord, Pageable pageable);

  void updateData();

  long getMaxRecipeNum();

  void saveRecipe(Recipe recipe);

  Map<String, List<String>> getAllRecipeKind();

  List<RecipeItemClient> getRandomRecipesByRecipeKind(String kind);

  List<RecipeItemClient> getRandomRecipesByUserTags(String tags);

  RecipeItemClient getRecipeItemClientByRecipeNum(Long recipeNum);

  RecipeNutritionClient getRecipeNutritionByRecipeNum(Long recipeNum);

  void save(long recipeNum, String mainIngredients);

  void saveKindRecipe(long recipeNum,String []kindName);

  boolean deleteRecipe(long recipeNum);
}
