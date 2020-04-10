package com.application.dao;

import com.application.entity.server.RecipeNutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeNutritionDao extends JpaRepository<RecipeNutrition, Long> {
  @Query(
      value = "select nutritions from recipe_nutrition where recipe_num limit 1",
      nativeQuery = true)
  String getNutritionByRecipeNum(Long recipeNum);
}
