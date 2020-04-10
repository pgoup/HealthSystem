package com.application.dao;

import com.application.entity.server.FoodNutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface FoodNutritionDao extends JpaRepository<FoodNutrition, Long> {

  @Transactional
  @Query(value = "update food_nutrition set food_num =?2 where food_name=?1", nativeQuery = true)
  @Modifying
  void updateFoodNum(String foodName, Long foodNum);
}
