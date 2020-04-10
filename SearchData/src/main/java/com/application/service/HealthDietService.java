package com.application.service;

import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.HealthDietClient;

import java.util.List;
import java.util.Map;

/** @author PG */
public interface HealthDietService {
  /** 获取所有的一级健康饮食类型 */
  List<String> getAllHealthDietKind();
  Map<String,List<String>> getAllClassifies();

  /** 根据一级健康饮食类型获取具体的饮食对象 */
  List<String> getHealthDietNameByDietKind(String healthDietKind);

  /** 根据二级分类获取相对应的所有食谱 */
  List<RecipeItemClient> getRecipesByDietName(String healthDietName);

  /** 根据二级分类的名称获取相应的信息 */
  HealthDietClient getDietByHealthDietName(String dietName);

  /**
   * 根据一级分类获取二级分类的集合
   *
   * @param dietKind
   * @return
   */
  List<String> getDietNameByDietKind(String dietKind);
}
