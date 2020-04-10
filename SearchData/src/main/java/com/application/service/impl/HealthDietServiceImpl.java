package com.application.service.impl;

import com.application.dao.HealthDietDao;
import com.application.dao.RecipeKindDao;
import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.HealthDietClient;
import com.application.service.HealthDietService;
import com.application.util.ConvertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/** @author PG */
@Service
public class HealthDietServiceImpl implements HealthDietService {

  @Resource private HealthDietDao healthDietDao;
  @Resource private RecipeKindDao recipeKindDao;

  /** 获取所有的一级健康饮食类型 */
  @Override
  public List<String> getAllHealthDietKind() {
    List<String> result = new ArrayList<>();
    for (Object o : healthDietDao.getAllHealthDietKind()) {
      result.add(o.toString());
    }
    System.out.println("一级目录个数为：" + result.size());

    return result;
  }

  @Override
  public Map<String, List<String>> getAllClassifies() {
    Map<String, List<String>> results = new LinkedHashMap<>();
    // results.put("家常菜", recipeKindDao.getAllRecipeKind());
    for (Object object : healthDietDao.getAllHealthDietKind()) {
      results.put(object.toString(), getHealthDietNameByDietKind(object.toString()));
    }
    return results;
  }

  /** 根据一级健康饮食类型获取具体的饮食对象 */
  @Override
  public List<String> getHealthDietNameByDietKind(String healthDietKind) {
    List<String> results = new ArrayList<>();
    for (String o : healthDietDao.getHealthDietNameByDietKind(healthDietKind)) {
      results.add(o);
    }
    return results;
  }

  /** 根据二级分类获取相对应的所有食谱 */
  @Override
  public List<RecipeItemClient> getRecipesByDietName(String healthDietName) {
    int dietId = healthDietDao.getNumberByName(healthDietName);
    return ConvertUtils.recipesConvertToContentItemClient(healthDietDao.getRecipesByDietId(dietId));
  }

  /** 根据二级分类的名称获取相应的信息 */
  @Override
  public HealthDietClient getDietByHealthDietName(String dietName) {
    List<Object[]> objects = healthDietDao.getDietByHealthDietName(dietName);
    System.out.println("对象属性个数为：" + objects.get(0).length);
    return new HealthDietClient(
        objects.get(0)[0].toString(),
        objects.get(0)[1].toString(),
        objects.get(0)[2].toString(),
        objects.get(0)[3].toString(),
        objects.get(0)[4].toString(),
        objects.get(0)[5].toString(),
        objects.get(0)[6].toString());
  }

  @Override
  public List<String> getDietNameByDietKind(String dietKind) {
    return healthDietDao.getDietNameByDietKind(dietKind);
  }
}
