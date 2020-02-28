package com.application.service;

import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.RecipeClient;
import com.application.entity.server.Recipe;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeClientService {

  List<RecipeItemClient> getRecipesByRecipeKind(String recipeKind, Pageable pageable);

  List<RecipeItemClient> getRecipesByHealthDiet(String healthDiet, Pageable pageable);

  List<RecipeItemClient> getRecipesByFunctionBenefit(String functionBenefit, Pageable pageable);

  RecipeClient getRecipeByNum(String recipeNum, Long userAccount);

  List<RecipeItemClient> getRecipesByKeyWord(String keyWord, Pageable pageable);

  void updateData();

  long getMaxRecipeNum();

  void saveRecipe(Recipe recipe);

  List<String> getAllRecipeKind();

  List<RecipeItemClient> getRandomRecipesByRecipeKind(String kind);

  List<RecipeItemClient> getRandomRecipesByUserTags(String tags);

  RecipeItemClient getRecipeItemClientByRecipeNum(Long recipeNum);
}
