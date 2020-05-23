package com.application.dao;

import com.application.entity.server.HealthDiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthDietDao extends JpaRepository<HealthDiet, Long> {

  @Query(
      value = "select health_diet_number from health_diet where health_diet_name = ?1 limit 1",
      nativeQuery = true)
  Integer getNumberByName(String name);

  @Query(value = "select * from health_diet where health_diet_name=?1 limit 1", nativeQuery = true)
  HealthDiet getByName(String name);

  @Query(
      value = "select health_diet_number from health_diet order by health_diet_id desc limit 1",
      nativeQuery = true)
  Integer getNum();

  @Query(value = "select distinct diet_kind from health_diet", nativeQuery = true)
  List<Object> getAllHealthDietKind();

  @Query(value = "select health_diet_name  from health_diet where diet_kind=?1", nativeQuery = true)
  List<String> getHealthDietNameByDietKind(String healthDietKind);

  @Query(
      value =
          "select recipe_name,pic_path,kind,view_count,collect_count,recipe_num from recipe join health_diet_recipe on recipe.recipe_number = health_diet_recipe.recipe_number and health_diet_recipe.health_diet_number=?1",
      nativeQuery = true)
  List<Object[]> getRecipesByDietId(int health_diet_id);

  @Query(
      value =
          "select diet_kind,health_diet_name,diet_method,suitable,suitable_foods,taboo,taboo_foods from health_diet where health_diet_name = ?1 limit 1",
      nativeQuery = true)
  List<Object[]> getDietByHealthDietName(String dietName);

  @Query(
      value = "select health_diet_name from health_diet where diet_kind = ?1",
      nativeQuery = true)
  List<String> getDietNameByDietKind(String dietKind);

  @Query(value = "select health_diet_name ,weight from health_diet", nativeQuery = true)
  List<Object[]> getAllDietNameAndWeight();
}
