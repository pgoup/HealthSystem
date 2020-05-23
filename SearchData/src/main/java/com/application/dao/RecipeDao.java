package com.application.dao;

import com.application.entity.server.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/** @author PG */
@Repository
public interface RecipeDao extends JpaRepository<Recipe, Long> {

  /** 获取最大的食谱编号 */
  @Query(
      value = "select recipe_num from recipe order by recipe_id desc limit 1",
      nativeQuery = true)
  Long getMaxRecipeNum();

  @Query(value = "select pic_path from recipe where recipe_num=?1 limit 1", nativeQuery = true)
  String getPicByRecipeNum(Long recipeNum);

  @Query(
      value = "select recipe_num from recipe where recipe_name=?1 and author_account=?2 limit 1",
      nativeQuery = true)
  Long getNumByRecipeNameAndAuthorAccount(String recipeName, Long authorAccount);

  @Query(value = "select recipe_num,author from recipe", nativeQuery = true)
  List<Object[]> getAllRecipeItem();

  @Query(value = "select * from recipe", nativeQuery = true)
  List<Recipe> getAllRecipes();

  /** 根据食谱名获取食谱 */
  /* @Query(value = "select * from recipe where recipe_name = ?1 limit 1", nativeQuery = true)
  Recipe getByName(String name);*/

  @Query(value = "select * from recipe where recipe_num=?1 limit 1", nativeQuery = true)
  Recipe getByNum(long recipeNum);

  @Query(value = "select recipe_num from recipe where recipe_name=?1 limit 1", nativeQuery = true)
  Long getNumByName(String name);

  /** 根据食谱类别获取相应的所有的食谱 */
  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author from recipe,recipes_and_kinds  where recipe.recipe_num=recipes_and_kinds.recipe_num and recipes_and_kinds.recipe_kind_num=?1 ",
      countQuery =
          "select count(*) from recipe join recipes_and_kinds on recipe.recipe_num=recipes_and_kinds.recipe_num and recipes_and_kinds.recipe_kind_num=?1",
      nativeQuery = true)
  List<Object[]> getRecipesByRecipeKind(int recipeKindNum, Pageable pageable);

  /** 根据健康饮食类型获取所有的食谱 */
  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author from recipe , health_diet_recipe where recipe.recipe_num=health_diet_recipe.recipe_num and health_diet_recipe.health_diet_num=?1",
      countQuery =
          "select count(*) from recipe join health_diet_recipe on  recipe.recipe_num=health_diet_recipe.recipe_num and health_diet_recipe.health_diet_num=?1",
      nativeQuery = true)
  List<Object[]> getRecipesByHealthDiet(int healthDietNumber, Pageable pageable);

  /** 根据功能性好处获取相应的所有食谱 */
  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author from recipe , function_benefit_recipe where recipe.recipe_id=function_benefit_recipe.recipe_id and function_benefit_recipe.function_benefit_id=?1",
      countQuery =
          "select count(*) from recipe  join function_benefit_recipe on recipe.recipe_num=function_benefit_recipe.recipe_num and function_benefit_recipe.function_benefit_num=?1",
      nativeQuery = true)
  List<Object[]> getRecipesByFunctionBenefit(int benefitNum, Pageable pageable);

  /** 获取收藏次数前500的食谱 */
  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author "
              + "from recipe  order by collect_count desc limit 500",
      nativeQuery = true)
  List<Object[]> getRecipesByCollectCount();

  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author from recipe  join recipes_and_kinds "
              + "where recipe.recipe_num=recipes_and_kinds.recipe_num and recipes_and_kinds.recipe_kind_num=?1 "
              + "order by recipe.collect_count limit 100",
      nativeQuery = true)
  List<Object[]> getRecipesByKindNum(int kindNum);

  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author from recipe where recipe_name like %?1% or key_ingredient like %?1% ",
      countQuery = "select count(*) from recipe where reipe_name like %?1% or key_ingredient %?1%",
      nativeQuery = true)
  List<Object[]> getRecipesByKeyWord(String keyWord, Pageable pageable);

  @Modifying
  @Query(value = "update recipe set collect_count=?2 where recipe_num=?1", nativeQuery = true)
  @Transactional
  void updateRecipeCollectedCount(long recipeNum, long collectedCount);

  @Modifying
  @Query(value = "update recipe set view_count=?2 where recipe_num=?1", nativeQuery = true)
  @Transactional
  void updateRecipeViewCount(long recipeNum, long viewCount);

  @Modifying
  @Transactional
  @Query(value = "update recipe set author_account = ?2 where author=?1", nativeQuery = true)
  void updataAuthorAccountByAuthorName(String authorName, Long authorAccount);

  @Modifying
  @Transactional
  @Query(value = "update recipe set key_ingredient =?2  where recipe_num =?1", nativeQuery = true)
  void updateRecipeKeyIngre(long recipeNum, String keyIngredient);

  @Query(value = "select collect_count from recipe where recipe_num=?1", nativeQuery = true)
  long getCollectedCountByRecipeNum(long recipeNum);

  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe.recipe_num,author from recipe where recipe_num=?1 limit 1",
      nativeQuery = true)
  List<Object[]> getRecipeItemByRecipeNum(Long recipeNum);

  @Query(
      value = "select main_ingredient from recipe where recipe_num=?1 limit 1",
      nativeQuery = true)
  String getMainIngredientByRecipeNum(Long recipeNum);

  @Query(
      value = "select recipe_name,pic_path from recipe where recipe_num=?1 limit 1",
      nativeQuery = true)
  List<Object[]> getNameAndPicByRecipeNum(Long recipeNum);
}
