package com.application.dao;

import com.application.entity.server.Nutritions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface NutritionsDao extends JpaRepository<Nutritions, Long> {
  @Query(value = "select * from nutritions where food_num=?1 limit 1", nativeQuery = true)
  Nutritions getByFoodNum(Long foodNum);

  @Modifying
  @Query(
      value =
          "update nutritions set vitaminb1=?1,vitaminb2=?2,vitaminb6=?3 ,vitaminb12=?4 where food_num=?5",
      nativeQuery = true)
  @Transactional
  void updateVitimB(float vb1, float vb2, float vb6, float vb12,Long footNum);
}
