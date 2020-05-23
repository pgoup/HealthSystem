package com.application.dao;

import com.application.entity.server.UserRecipes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** @author PG */
@Repository
public interface UserRecipesDao extends JpaRepository<UserRecipes, Long> {

  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author from recipe , user_recipes where recipe.recipe_num=user_recipes.recipe_num and user_recipes.user_account=?1",
      countQuery =
          "select count(*) from recipe  join user_recipes on recipe.recipe_num=user_recipes.recipe_num and user_recipes.user_account=?1",
      nativeQuery = true)
  List<Object[]> getMimeRecipes(Long userAccount, Pageable pageable);

  @Query(value = "select * from user_recipes where recipe_num=?1 limit 1", nativeQuery = true)
  UserRecipes getAllByRecipeNum(long recipeNum);
}
