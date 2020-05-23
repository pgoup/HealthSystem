package com.application.dao;

import com.application.entity.server.RecipeKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/** @author PG */
@Repository
public interface RecipeKindDao extends JpaRepository<RecipeKind, Long> {

  @Query(
      value = "select recipe_kind_num from recipe_kind  order by recipe_kind_id desc limit 1",
      nativeQuery = true)
  Integer getNum();

  @Query(
      value = "select *  from recipe_kind where recipe_kind_name=?1 limit 1 ",
      nativeQuery = true)
  RecipeKind getAllByKindName(String kindName);

  @Query(
      value = "select recipe_kind_num from recipe_kind where recipe_kind_name = ?1 limit 1",
      nativeQuery = true)
  Integer getNumByName(String name);

  @Query(value = "select recipe_kind_name from recipe_kind where normal = 1", nativeQuery = true)
  List<String> getAllRecipeKind();

  @Query(
      value = "select recipe_kind_name , pic from recipe_kind where normal =1",
      nativeQuery = true)
  List<Object[]> getAllCommonRecipeKind();

  @Query(
      value = "select recipe_kind_num from recipe_kind order by recipe_kind_id desc  limit 1",
      nativeQuery = true)
  Integer getMaxNum();

  @Query(value = "update recipe_kind set weight=?2 where recipe_kind_name=?1", nativeQuery = true)
  @Transactional
  @Modifying
  void updateWeight(String recipeKindName, int weight);

  @Query(value = "update recipe_kind set pic=?2 where recipe_kind_name=?1", nativeQuery = true)
  @Transactional
  @Modifying
  void updatepic(String recipeKindName, String pic);

  @Query(
      value = "select weight from recipe_kind where recipe_kind_name=?1 limit 1",
      nativeQuery = true)
  Integer getWeightByKindName(String kindName);
}
