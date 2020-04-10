package com.application.dao;

import com.application.entity.server.HealthDietRecipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface HealthDietRecipesDao extends JpaRepository<HealthDietRecipes, Long> {
  @Query(
      value = "select recipe_num from health_diet_recipe where health_diet_num=?1",
      nativeQuery = true)
  List<BigInteger> getAllNumByHealthDietNum(Long healthDietNum);
}
