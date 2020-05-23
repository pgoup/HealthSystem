package com.application.dao;

import com.application.entity.server.UserAddNutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** @author PG */
@Repository
public interface UserAddNutritionDao extends JpaRepository<UserAddNutrition, Long> {
  @Query(value = "select * from user_add_nutrition where user_account=?1", nativeQuery = true)
  List<UserAddNutrition> findByUserAccount(String userAccount);

  @Query(
      value =
          "select * from user_add_nutrition where user_account=?1 and nutrition_name=?2 order by add_nutrition_id limit 1",
      nativeQuery = true)
  UserAddNutrition findByUserAccountAndNutritionName(String userAccount, String name);
}
