package com.application.dao;

import com.application.entity.server.UserCollectRecipes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/** @author PG */
@Repository
public interface UserCollectRecipesDao extends JpaRepository<UserCollectRecipes, Long> {
  @Modifying
  @Transactional
  @Query(
      value = "delete from user_collect_recipes where user_account=?1 and recipe_num=?2",
      nativeQuery = true)
  void deleteByAccountAndRecipeNum(Long userAccount, Long recipeNum);

  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author from recipe , user_collect_recipes where recipe.recipe_num = user_collect_recipes.recipe_num and user_collect_recipes.user_account = ?1",
      countQuery =
          "select count(*) from recipe  join user_collect_recipes on recipe.recipe_num=user_collect_recipes.recipe_num and user_collect_recipes.user_account=?1",
      nativeQuery = true)
  List<Object[]> getCollectedRecipesByUserAccount(Long userAccount, Pageable pageable);

  @Query(
      value = "select * from user_collect_recipes where user_account=?1 and recipe_num=?2 limit 1",
      nativeQuery = true)
  UserCollectRecipes getCollectedRecipes(Long userAccount, Long recipeNumber);
}
