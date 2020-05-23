package com.application.dao;

import com.application.entity.server.RecipeKindRecipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.TimerTask;

/** @author PG */
@Repository
public interface RecipeKindRecipesDao extends JpaRepository<RecipeKindRecipes, Long> {

  @Query(
      value = "select id from recipes_and_kinds where recipe_num=?1 and recipe_kind_num=?2",
      nativeQuery = true)
  Long getRecipeId(Long recipeNum, int recipeKindNum);

  @Query(
      value = "select recipe_num from recipes_and_kinds where recipe_kind_num=?1 limit 1",
      nativeQuery = true)
  Long getRecipeNum(int recipeKindNum);
  /**
   * 获取某个种类的数量
   *
   * @param kindName
   * @return
   */
  @Query(
      value =
          "select count(recipe_kind_id) from recipes_and_kinds join recipe_kind on recipes_and_kinds.recipe_kind_num = recipe_kind.recipe_kind_num and recipe_kind_name=?1",
      countQuery =
          "select count(recipe_kind_id) from recipes_and_kinds , recipe_kind on recipes_and_kinds.recipe_kind_num = recipe_kind.recipe_kind_num and recipe_kind_name=?1",
      nativeQuery = true)
  int getCountByKind(String kindName);

  @Query(
      value = "select recipe_num from recipes_and_kinds where recipe_kind_num=?1",
      nativeQuery = true)
  List<BigInteger> getAllRecipeNumByRecipeKind(Long recipeKindNum);
}
