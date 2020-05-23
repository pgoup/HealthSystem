package com.application.dao;

import com.application.entity.server.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/** @author PG */
@Repository
public interface FoodDao extends JpaRepository<Food, Long> {
  /** 获取所有的一级食物分类 */
  @Query(value = "select distinct first_kind from food ", nativeQuery = true)
  List<String> getAllFirstKindFood();

  /** 根据一级分类获取二级分类 */
  @Query(value = "select distinct second_kind from food where first_kind = ?1", nativeQuery = true)
  List<String> getSecondKindByFirstKind(String firstKindFood);

  /** 根据二级分类获取相应的食物 */
  @Query(value = "select food_name,pic_path from food where second_kind = ?1 ", nativeQuery = true)
  List<Object[]> getFoodsBySecondKind(String secondKind);

  /** 根据名称获取具体的食物信息 */
  @Query(
      value =
          "select food_name,pic_path,health_worth,introduction,benefit,suitable_people,taboo_people,nutritional_ingredient from food where food_name = ?1 limit 1",
      nativeQuery = true)
  List<Object[]> getFoodByName(String name);

  @Query(value = "select food_name , nutritional_ingredient from food", nativeQuery = true)
  List<Object[]> getAllFoodNameAndNutrition();

  @Query(value = "select number from food where food_name=?1 limit 1", nativeQuery = true)
  String getFoodNumByName(String foodName);

  @Modifying
  @Transactional
  @Query(value = "update food set nutritional_ingredient =?2  where number =?1", nativeQuery = true)
  void updateIngredient(long number, String nutritional_ingredient);
}
