package com.application.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.application.dao.*;
import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.RecipeMeasureItem;
import com.application.entity.client.RecipeNutritionClient;
import com.application.entity.server.*;
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
import java.text.DecimalFormat;
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
  @Resource private NutritionsDao nutritionsDao;
  @Resource private FoodDao foodDao;
  @Resource private RecipeNutritionDao recipeNutritionDao;
  @Resource private UserRecipesDao userRecipesDao;

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
    boolean isConcern;
    boolean isCollected;
    if (userAccount == null) {
      isCollected = false;
      isConcern = false;
    } else {
      isConcern =
          concernedUserDao.getIdByUserAccountAndConcernedAccount(
                  userAccount, recipe.getAuthorAccount())
              != null;

      isCollected =
          collectRecipesDao.getCollectedRecipes(userAccount, recipe.getRecipeNum()) != null;
    }
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
              datas.get(i)[4] == null ? 0 : (Integer) datas.get(i)[4],
              datas.get(i)[6].toString());
      String key = "default_" + i;
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
                datas.get(i)[4] == null ? 0 : (Integer) datas.get(i)[4],
                datas.get(i)[6].toString());
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
    save(recipe.getRecipeNum(), recipe.getMainIngredient());
    recipeDao.saveAndFlush(recipe);
  }

  @Override
  public Map<String, List<String>> getAllRecipeKind() {
    Map<String, List<String>> allRecipeKinds = new LinkedHashMap<>();
    allRecipeKinds.put("家常菜", recipeKindDao.getAllRecipeKind());
    allRecipeKinds.put("人群膳食", healthDietDao.getHealthDietNameByDietKind("人群膳食"));
    allRecipeKinds.put("疾病调理", healthDietDao.getHealthDietNameByDietKind("疾病调理"));
    allRecipeKinds.put("功能性调理", healthDietDao.getHealthDietNameByDietKind("功能性调理"));
    allRecipeKinds.put("脏腑调理", healthDietDao.getHealthDietNameByDietKind("脏腑调理"));
    return allRecipeKinds;
  }

  @Override
  public List<RecipeItemClient> getRandomRecipesByRecipeKind(String kind) {
    List<RecipeItemClient> clients = new ArrayList<>();
    int count;
    if (kind.equals("default")) count = 500;
    else count = recipeKindRecipesDao.getCountByKind(kind);
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
    String[] values = tags.split("&&"); // 将标签字符串转换为单个的标签内容
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
    return itemClients;
  }

  // 随机选择某个指定数量类别的食谱
  private List<RecipeItemClient> getRandomCountByKind(String kind, int count, int weight) {
    System.out.println("类别为：" + kind + "指定类别的个数：" + count);
    List<BigInteger> recipeNums = new ArrayList<>();
    long kindNum = 0L;
    /* if (weight > 1) {
      kindNum = healthDietDao.getNumberByName(kind);
      recipeNums = dietRecipesDao.getAllNumByHealthDietNum(kindNum);
    } else {*/
    kindNum = recipeKindDao.getNumByName(kind);
    recipeNums = recipeKindRecipesDao.getAllRecipeNumByRecipeKind(kindNum);

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

  // 根据食谱编号获取食谱的营养参数
  @Override
  public RecipeNutritionClient getRecipeNutritionByRecipeNum(Long recipeNum) {
    RecipeNutritionClient recipeNutritionClient = new RecipeNutritionClient();
    List<Object[]> objects = recipeDao.getNameAndPicByRecipeNum(recipeNum);
    if (objects != null) {
      recipeNutritionClient.setRecipeName(objects.get(0)[0].toString());
      recipeNutritionClient.setRecipeImage(
          ConvertUtils.picConvertToByte(objects.get(0)[1].toString()));
    }
    String nutrition = recipeNutritionDao.getNutritionByRecipeNum(recipeNum);
    if (nutrition == null) nutrition = "";
    recipeNutritionClient.setNutritions(nutrition);
    return recipeNutritionClient;
  }

  public void save(long recipeNum, String mainIngredients) {
    // RecipeNutritionClient recipeNutritionClient = new RecipeNutritionClient();
    // String mainIngredients = recipeDao.getMainIngredientByRecipeNum(recipeNum);
    String[] ingredients = mainIngredients.split("##");
    Map<String, Float> results = new LinkedHashMap<>();
    for (String ingredient : ingredients) {
      if (!ingredient.isEmpty() && !ingredient.equals("&&")) {
        System.out.println("食材名称为" + ingredient);
        String[] names = ingredient.split("&&");
        String name = ingredient.split("&&")[0];
        String count = "";
        if (names.length > 1) count = ingredient.split("&&")[1];
        if (count.contains("克")) {
          int num = 0;
          try {
            num = Integer.valueOf(count.substring(0, count.length() - 1));

          } catch (Exception e) {
            continue;
          }

          DecimalFormat decimalFormat = new DecimalFormat("0.00");
          float d = Float.parseFloat(decimalFormat.format(num / 100));

          if (results.containsKey(name)) {
            results.put(name, results.get(name) + d);
          }
          results.put(name, d);
        }
      }
    }
    Nutritions nutritions = new Nutritions();
    System.out.println("食谱名称为：" + recipeNum);
    Iterator<String> iterator = results.keySet().iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      String foodNum = foodDao.getFoodNumByName(key);
      Nutritions n = null;
      if (foodNum != null) n = nutritionsDao.getByFoodNum(Long.parseLong(foodNum));
      if (n != null) {
        System.out.println("食物的编号为：" + foodNum);
        float m = n.getCa();
        System.out.println("食物的名称为：" + key + "食物的钙含量：" + n.getCa() + " 比例：" + results.get(key));
        nutritions.setCa(nutritions.getCa() + n.getCa() * results.get(key));
        nutritions.setCarbohydrate(
            nutritions.getCarbohydrate() + n.getCarbohydrate() * results.get(key));
        nutritions.setCholesterol(
            nutritions.getCholesterol() + n.getCholesterol() * results.get(key));
        nutritions.setCu(nutritions.getCu() + n.getCu() * results.get(key));
        nutritions.setFat(nutritions.getFat() + n.getFat() * results.get(key));
        nutritions.setFe(nutritions.getFe() + n.getFe() * results.get(key));
        // nutritions.setFoodNum(nutritions.getCa()+n.getCa()*results.get(key));
        nutritions.setHeat(nutritions.getHeat() + n.getHeat() * results.get(key));
        nutritions.setI(nutritions.getI() + n.getI() * results.get(key));
        nutritions.setKa(nutritions.getKa() + n.getKa() * results.get(key));
        nutritions.setMg(nutritions.getMg() + n.getMg() * results.get(key));
        nutritions.setMn(nutritions.getMn() + n.getMn() * results.get(key));
        nutritions.setNa(nutritions.getNa() + n.getNa() * results.get(key));
        nutritions.setP(nutritions.getP() + n.getP() * results.get(key));
        nutritions.setProtein(nutritions.getProtein() + n.getProtein() * results.get(key));
        nutritions.setVitaminA(nutritions.getVitaminA() + n.getVitaminA() * results.get(key));
        nutritions.setVitaminB1(nutritions.getVitaminB1() + n.getVitaminB1() * results.get(key));
        nutritions.setVitaminB2(nutritions.getVitaminB2() + n.getVitaminB2() * results.get(key));
        nutritions.setVitaminB6(nutritions.getVitaminB6() + n.getVitaminB6() * results.get(key));
        nutritions.setVitaminB12(nutritions.getVitaminB12() + n.getVitaminB12() * results.get(key));
        nutritions.setVitaminC(nutritions.getVitaminC() + n.getVitaminC() * results.get(key));
        nutritions.setVitaminD(nutritions.getVitaminD() + n.getVitaminD() * results.get(key));
        nutritions.setVitaminE(nutritions.getVitaminE() + n.getVitaminE() * results.get(key));
        nutritions.setZn(nutritions.getZn() + n.getZn() * results.get(key));
      }
    }
    StringBuilder builder = new StringBuilder();
    builder
        .append("热量")
        .append("&&")
        .append(nutritions.getHeat())
        .append(" 千卡")
        .append("##")
        .append("胆固醇")
        .append("&&")
        .append(nutritions.getCholesterol())
        .append(" 毫克")
        .append("##")
        .append("蛋白质")
        .append("&&")
        .append(nutritions.getProtein())
        .append(" 克")
        .append("##")
        .append("碳水化合物")
        .append("&&")
        .append(nutritions.getCarbohydrate())
        .append(" 克")
        .append("##")
        .append("脂肪")
        .append("&&")
        .append(nutritions.getFat())
        .append(" 克")
        .append("##")
        .append("维生素A")
        .append("&&")
        .append(nutritions.getVitaminA())
        .append(" 微克")
        .append("##")
        .append("维生素B1")
        .append("&&")
        .append(nutritions.getVitaminB1())
        .append(" 毫克")
        .append("##")
        .append("维生素B2")
        .append("&&")
        .append(nutritions.getVitaminB2())
        .append(" 毫克")
        .append("##")
        .append("维生素B6")
        .append("&&")
        .append(nutritions.getVitaminB6())
        .append(" 毫克")
        .append("##")
        .append("维生素B12")
        .append("&&")
        .append(nutritions.getVitaminB12())
        .append(" 毫克")
        .append("##")
        .append("维生素C")
        .append("&&")
        .append(nutritions.getVitaminC())
        .append(" 毫克")
        .append("##")
        .append("维生素D")
        .append("&&")
        .append(nutritions.getVitaminD())
        .append(" 毫克")
        .append("##")
        .append("维生素E")
        .append("&&")
        .append(nutritions.getVitaminE())
        .append(" 毫克")
        .append("##")
        .append("钾")
        .append("&&")
        .append(nutritions.getKa())
        .append(" 毫克")
        .append("##")
        .append("钙")
        .append("&&")
        .append(nutritions.getCa())
        .append(" 毫克")
        .append("##")
        .append("钠")
        .append("&&")
        .append(nutritions.getNa())
        .append(" 毫克")
        .append("##")
        .append("镁")
        .append("&&")
        .append(nutritions.getMg())
        .append(" 毫克")
        .append("##")
        .append("磷")
        .append("&&")
        .append(nutritions.getP())
        .append(" 毫克")
        .append("##")
        .append("铁")
        .append("&&")
        .append(nutritions.getFe())
        .append(" 毫克")
        .append("##")
        .append("锌")
        .append("&&")
        .append(nutritions.getZn())
        .append(" 毫克")
        .append("##")
        .append("铜")
        .append("&&")
        .append(nutritions.getCu())
        .append(" 毫克")
        .append("##")
        .append("锰")
        .append("&&")
        .append(nutritions.getMn())
        .append(" 毫克")
        .append("##")
        .append("碘")
        .append("&&")
        .append(nutritions.getI())
        .append(" 毫克")
        .append("##");
    RecipeNutrition recipeNutrition = new RecipeNutrition(recipeNum, builder.toString());
    recipeNutritionDao.save(recipeNutrition);
  }

  // 保存食谱类别与食谱的联系
  @Override
  public void saveKindRecipe(long recipeNum, String[] kindName) {

    for (String name : kindName) {
      RecipeKind recipeKind = recipeKindDao.getAllByKindName(name);
      if (recipeKind.getNormal() == 1) {
        RecipeKindRecipes kindRecipes =
            new RecipeKindRecipes(recipeKind.getRecipeKindNum(), recipeNum);
        recipeKindRecipesDao.save(kindRecipes);
      } else {
        int dietNum = healthDietDao.getNumberByName(name);
        HealthDietRecipes dietRecipes = new HealthDietRecipes(recipeNum, dietNum);
        dietRecipesDao.save(dietRecipes);
      }
    }
  }

  @Override
  public boolean deleteRecipe(long recipeNum) {
    try {
      Recipe recipe = recipeDao.getByNum(recipeNum);
      recipeDao.delete(recipe);
      UserRecipes userRecipes = userRecipesDao.getAllByRecipeNum(recipeNum);
      userRecipesDao.delete(userRecipes);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
