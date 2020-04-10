package com.application.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.application.dao.*;
import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.RecipeMeasureItem;
import com.application.entity.server.Recipe;
import com.application.entity.client.RecipeClient;
import com.application.redis.RecipeRedisService;
import com.application.service.RecipeClientService;
import com.application.service.UserService;
import com.application.util.ConvertUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.math.BigInteger;
import java.util.*;

/** @author PG */
@Service
public class RecipeClientServiceImpl implements RecipeClientService {

  @Resource private RecipeKindDao recipeKindDao;
  @Resource private RecipeDao recipeDao;
  @Resource private HealthDietDao healthDietDao;
  @Resource private FunctionalBenefitDao functionalBenefitDao;
  @Resource private RecipeRedisService recipeRedisService;
  @Resource private UserConcernedUserDao concernedUserDao;
  @Resource private UserCollectRecipesDao collectRecipesDao;
  @Resource private UserService userService;
  @Resource private RecipeKindRecipesDao recipeKindRecipesDao;
  @Resource private HealthDietRecipesDao dietRecipesDao;

  @Override
  public List<RecipeItemClient> getRecipesByRecipeKind(String recipeKind, Pageable pageable) {
    int recipeKindNum = recipeKindDao.getNumByName(recipeKind);
    return ConvertUtils.recipesConvertToContentItemClient(
        recipeDao.getRecipesByRecipeKind(recipeKindNum, pageable));
  }

  @Override
  public List<RecipeItemClient> getRecipesByHealthDiet(String healthDiet, Pageable pageable) {
    int healthDietNumber = healthDietDao.getNumberByName(healthDiet);
    return ConvertUtils.recipesConvertToContentItemClient(
        recipeDao.getRecipesByHealthDiet(healthDietNumber, pageable));
  }

  @Override
  public List<RecipeItemClient> getRecipesByFunctionBenefit(
      String functionBenefit, Pageable pageable) {
    int benefitNum = functionalBenefitDao.getNumByName(functionBenefit);
    return ConvertUtils.recipesConvertToContentItemClient(
        recipeDao.getRecipesByFunctionBenefit(benefitNum, pageable));
  }

  /**
   * 根据食谱编号获取食谱
   *
   * @param recipeNum
   * @param userAccount 用户的账号，用以查询该用户是否关注过该食谱作者和是否收藏过该食谱
   * @return
   */
  @Override
  public RecipeClient getRecipeByNum(String recipeNum, Long userAccount) {
    if (recipeNum == null) {
      return null;
    }
    Recipe recipe = recipeDao.getByNum(Long.valueOf(recipeNum));
    boolean isConcern =
        concernedUserDao.getIdByUserAccountAndConcernedAccount(
                userAccount, recipe.getAuthorAccount())
            != null;
    boolean isCollected =
        collectRecipesDao.getCollectedRecipes(userAccount, recipe.getRecipeNum()) != null;
    String authorImagePath = userService.getPicPathByUserAccount(recipe.getAuthorAccount());

    return new RecipeClient(
        recipe.getRecipeNum(),
        recipe.getKind(),
        recipe.getRecipeName(),
        ConvertUtils.picConvertToByte(recipe.getPicPath()),
        recipe.getMainIngredient(),
        recipe.getAccessories(),
        measureToRecipeMeasureItem(recipe.getRecipeNum(), recipe.getMeasure()),
        recipe.getViewCount(),
        recipe.getCollectCount(),
        recipe.getAuthor(),
        isCollected,
        isConcern,
        ConvertUtils.picConvertToByte(authorImagePath),
        recipe.getAuthorAccount());
  }

  @PostConstruct
  @Override
  public void updateData() {
    String[] kindNames = {
      "家常菜", "私家菜", "凉菜", "海鲜", "热菜", "汤粥", "素食", "酱料蘸料", "微波炉", "火锅底料", "甜品点心", "糕点主食", "干果制作",
      "卤酱", "时尚饮品"
    };
    int[] kindNums = {
      1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014
    };

    List<Object[]> datas = recipeDao.getRecipesByCollectCount();
    for (int i = 0; i < datas.size(); i++) {
      RecipeItemClient client =
          new RecipeItemClient(
              datas.get(i)[5].toString(),
              datas.get(i)[0].toString(),
              ConvertUtils.picConvertToByte(datas.get(i)[1].toString()),
              datas.get(i)[2].toString(),
              datas.get(i)[3] == null ? 0 : (Integer) datas.get(i)[3],
              datas.get(i)[4] == null ? 0 : (Integer) datas.get(i)[4]);
      String key = "默认_" + i;
      recipeRedisService.addCache(key, JSONObject.toJSONString(client));
    }
    for (int j = 0; j < kindNames.length; j++) {
      datas.clear();
      datas = recipeDao.getRecipesByKindNum(kindNums[j]);
      for (int i = 0; i < datas.size(); i++) {
        RecipeItemClient client =
            new RecipeItemClient(
                datas.get(i)[5].toString(),
                datas.get(i)[0].toString(),
                ConvertUtils.picConvertToByte(datas.get(i)[1].toString()),
                datas.get(i)[2].toString(),
                datas.get(i)[3] == null ? 0 : (Integer) datas.get(i)[3],
                datas.get(i)[4] == null ? 0 : (Integer) datas.get(i)[4]);
        String key = kindNames[j] + "_" + i;
        recipeRedisService.addCache(key, JSONObject.toJSONString(client));
      }
    }
  }

  /** 将制作方法的文字表述转换为其实体类 */
  private List<RecipeMeasureItem> measureToRecipeMeasureItem(long recipeNum, String measures) {
    List<RecipeMeasureItem> items = new ArrayList<>();
    String[] measure = measures.split("##");
    for (int i = 0; i < measure.length; i++) {
      String[] item = measure[i].split("&&");
      if (item.length == 2)
        items.add(
            new RecipeMeasureItem(recipeNum, i, item[0], ConvertUtils.picConvertToByte(item[1])));
      else
        items.add(new RecipeMeasureItem(recipeNum, i, "", ConvertUtils.picConvertToByte(item[0])));
    }
    return items;
  }

  @Override
  public List<RecipeItemClient> getRecipesByKeyWord(String keyWord, Pageable pageable) {
    return ConvertUtils.recipesConvertToContentItemClient(
        recipeDao.getRecipesByKeyWord(keyWord, pageable));
  }

  /**
   * 获取最大的食谱
   *
   * @return
   */
  @Override
  public long getMaxRecipeNum() {
    return recipeDao.getMaxRecipeNum();
  }

  @Override
  public void saveRecipe(Recipe recipe) {
    recipeDao.saveAndFlush(recipe);
  }

  @Override
  public List<String> getAllRecipeKind() {
    return recipeKindDao.getAllRecipeKind();
  }

  @Override
  public List<RecipeItemClient> getRandomRecipesByRecipeKind(String kind) {
    List<RecipeItemClient> clients = new ArrayList<>();
    int count;
    if (kind.equals("默认")) count = 500;
    else {
      count = recipeKindRecipesDao.getCountByKind(kind);
    }
    int max = 500;
    int min = 1;
    Random random = new Random();
    Set<Integer> nums = new HashSet<>();
    for (int i = 0; nums.size() < 10; i++) {
      int randomNum = random.nextInt(max) + min;
      while (randomNum >= count) randomNum = random.nextInt(max) + min;
      nums.add(randomNum);
    }
    Iterator<Integer> iterator = nums.iterator();
    while (iterator.hasNext()) {
      String key = kind + "_" + iterator.next();
      JSONObject object = JSONObject.parseObject(recipeRedisService.queryCache(key));
      RecipeItemClient client = JSONObject.toJavaObject(object, RecipeItemClient.class);
      if (client != null) clients.add(client);
    }
    return clients;
  }

  // 根据用户标签的权重值来进行查找推荐食谱
  @Override
  public List<RecipeItemClient> getRandomRecipesByUserTags(String tags) {
    String[] values = tags.split("&&");
    Map<String, Integer> weights = new HashMap<>();
    int sum = 0;
    for (String value : values) {
      int num = recipeKindDao.getWeightByKindName(value);
      sum += num;
      weights.put(value, num);
    }
    List<RecipeItemClient> itemClients = new ArrayList<>();

    for (int i = 0; i < values.length; i++) {
      int num = weights.get(values[i]) * 20 / sum;
      itemClients.addAll(getRandomCountByKind(values[i], num, weights.get(values[i])));
    }
    Collections.shuffle(itemClients);
    System.out.println("返回的食谱个数为：" + itemClients.size());
    return itemClients;
  }

  // 随机选择某个指定数量类别的食谱
  private List<RecipeItemClient> getRandomCountByKind(String kind, int count, int weight) {
    System.out.println("类别为：" + kind + "指定类别的个数：" + count);
    List<BigInteger> recipeNums = new ArrayList<>();
    long kindNum = 0L;
    if (weight > 1) {
      kindNum = healthDietDao.getNumberByName(kind);
      recipeNums = dietRecipesDao.getAllNumByHealthDietNum(kindNum);
    } else {
      kindNum = recipeKindDao.getNumByName(kind);
      recipeNums = recipeKindRecipesDao.getAllRecipeNumByRecipeKind(kindNum);
    }
    Random random = new Random();
    List<RecipeItemClient> recipeItemClients = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      int m = random.nextInt(recipeNums.size());

      long recipeNum = Long.parseLong(recipeNums.get(m).toString());
      recipeItemClients.add(getRecipeItemClientByRecipeNum(recipeNum));
      recipeNums.remove(m);
    }
    return recipeItemClients;
  }

  @Override
  public RecipeItemClient getRecipeItemClientByRecipeNum(Long recipeNum) {
    Object[] objects = recipeDao.getRecipeItemByRecipeNum(recipeNum).get(0);
    // System.out.println("对象的个数为：" + objects.length);
    return ConvertUtils.objectsToContentItemClient(objects);
  }
}
