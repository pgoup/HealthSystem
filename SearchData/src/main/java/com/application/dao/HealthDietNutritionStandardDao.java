package com.application.dao;

import com.application.entity.server.HealthDiet;
import com.application.entity.server.HealthDietNutritionStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** @author PG */
@Repository
public interface HealthDietNutritionStandardDao
    extends JpaRepository<HealthDietNutritionStandard, Long> {

  @Query(
      value =
          "select * from health_diet_nutrition_standard where health_diet_name=?1 and man_kind=?2 order by id desc limit 1",
      nativeQuery = true)
  HealthDietNutritionStandard getByHealthDietNameAndManKind(String name, int manKind);
}
