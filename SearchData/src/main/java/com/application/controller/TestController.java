package com.application.controller;

import com.application.dao.*;
import com.application.entity.server.*;
import com.application.redis.RecipeRedisService;
import com.application.service.RecipeClientService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/** @author PG */
@Controller
public class TestController {

  @Resource private RecipeKindDao recipeKindDao;
  @Resource private RecipeDao recipeDao;
  @Resource private RecipeKindRecipesDao recipeKindRecipesDao;
  @Resource private UserDao userDao;
  @Resource private UserRecipesDao userRecipesDao;
  @Resource private HealthDietDao healthDietDao;
  @Resource private FoodDao foodDao;
  @Resource private FoodNutritionDao foodNutritionDao;
  @Resource private NutritionsDao nutritionsDao;
  @Resource private RecipeClientService recipeClientService;
  @Resource private RecipeNutritionDao recipeNutritionDao;
  @Resource private HealthDietNutritionStandardDao standardDao;
  @Resource private RecipeAndFoodDao recipeAndFoodDao;

  @ResponseBody
  @RequestMapping("/test")
  public String test() throws Exception {
    List<Recipe> recipes = recipeDao.findAll();
    for (Recipe recipe : recipes) {
      String[] values = recipe.getMainIngredient().split("##");
      for (String value : values) {
        String[] v = value.split("&&");
        System.out.println("食材为：" + v[0]);
        String food = foodDao.getFoodNumByName(v[0]);
        if (food != null) {
          Long foodNum = Long.valueOf(food);

          RecipeAndFood recipeAndFood = new RecipeAndFood();
          recipeAndFood.setRecipeNum(recipe.getRecipeNum());
          recipeAndFood.setFoodNum(foodNum);
          recipeAndFoodDao.save(recipeAndFood);
        }
      }
    }
    return "success";
  }
  /* Map<Integer, String> map = new HashMap<>();
    map.put(1, "35");
    map.put(2, "104-125");
    map.put(3, "84-104");

    HealthDietNutritionStandard standard = new HealthDietNutritionStandard();
    standard.setManKind(3);
    // 一克碳水化合物等于4大卡,总能量50-60
    standard.setCarbohydrate("2.75-3.75");
    // 1克脂肪等于9大卡，12  总能量小于30%，2.66   3 小于25% 2.22
    standard.setFat("0-0.69");
    standard.setHealthDietName("糖尿病");
    standard.setHealthDietNum(1018);
    // 0.8-1
    standard.setProtein("0.8-1");
    standard.setHeat("22-25");
    standardDao.save(standard);

    return "success";
  }*/

  /*  int num = 0;
      List<Nutritions> nutritions = nutritionsDao.findAll();
      for (Nutritions nutritions1 : nutritions) {
        if (nutritions1.getHeat() == 0) num++;
      }

      System.out.println("符合要求的数据个数为：" + num + "总数据为：" + foodDao.findAll().size());
  */
  /*  List<RecipeNutrition> recipeNutritions = recipeNutritionDao.findAll();
   for (RecipeNutrition nutrition : recipeNutritions) {
     if (recipeDao.getByNum(nutrition.getRecipe_num()) != null) {
       String nu = nutrition.getNutritions();
       String value = nu.split("##")[0].split("&&")[1].split(" ")[0];
       if (!value.equals("0.0")) {
         System.out.println(nutrition.getRecipe_num());
         num++;
       }
     }
   }
   System.out.println("符合要求的数据个数为：" + num);

  */
  /* List<Recipe> recipes = recipeDao.findAll();
     Map<String, Long> map = new HashMap<>();
     for (Recipe recipe : recipes) {
       String value = recipe.getRecipeName() + "-" + recipe.getAuthorAccount();
       String kind = recipe.getKind();
       if (map.keySet().contains(value)) {
         recipeDao.deleteById(recipe.getId());
       } else map.put(value, recipe.getRecipeNum());
       int kindNum = recipeKindDao.getNumByName(kind);
       RecipeKindRecipes recipeKindRecipes = new RecipeKindRecipes();
       recipeKindRecipes.setRecipeKindNum(kindNum);
       recipeKindRecipes.setRecipeNum(map.get(value));
       if (recipeKindRecipesDao.getRecipeId(map.get(value), kindNum)==null)
         recipeKindRecipesDao.save(recipeKindRecipes);
     }

     System.out.println("食谱总数为：" + map.keySet().size());
  */
  /* List<FoodNutrition> foodNutritions = foodNutritionDao.findAll();
  // Set<String> names = new LinkedHashSet<>();
  Map<String, Float> result = new LinkedHashMap<>();
  for (FoodNutrition foodNutrition : foodNutritions) {
    String nutrition = foodNutrition.getNutrition().replaceAll("\\u00A0", "&");
    if (!nutrition.isEmpty()) {
      System.out.println(foodNutrition.getFoodNum() + "   " + nutrition);
      String[] nutritionName = nutrition.split("/");
      for (String name : nutritionName) {
        String[] values = name.split("&");

        String key = values[0];
        String value = "0";

        if (values.length != 1) {
          value = values[1].split(" ")[0];
        }
        System.out.println("key :" + key + " value:" + value);
        result.put(key, Float.valueOf(value));
      }
      saveNutrition(foodNutrition.getFoodNum(), result);
      result.clear();
    }
  }*/
  /* List<Object[]> values = foodDao.getAllFoodNameAndNutrition();
  能量(千卡) 260/蛋白质(克) 22.6/脂肪(克) 18.8 /碳水化合物(克) 0/胆固醇(毫克) 192 /灰分(克) 0.4 /维生素A(微克RE) 3 /胡萝卜素(微克) 0 /视黄醇(微克) 3 /硫胺素(毫克) 0.05 /核黄素(毫克) 0.1 /尼克酸(毫克) 1.5 /维生素C(毫克) 0 /维生素E(毫克) 0.01 /钙(毫克) 33 /磷(毫克) 33 /钾(毫克) 54 /钠(毫克) 101 /镁(毫克) 5 /铁(毫克) 1.1 /锌(毫克) 1.14 /硒(微克) 5.85 /铜(毫克) 0.09 /锰(毫克) 0.01
  int num = 0;
  for (Object[] object : values) {
    if (object[1].toString().isEmpty()) num++;
  }
  System.out.println("num:" + num + "   sum:" + values.size());*/

  /*
  List<Object[]> healthDiets = healthDietDao.getAllDietNameAndWeight();
  for (Object[] objects : healthDiets) {
    String healthName = objects[0].toString();
    recipeKindDao.updateWeight(healthName, Integer.parseInt(objects[1].toString()));
  }*/

  /*List<UserInfo> users = userDao.findAll();
  for (UserInfo userInfo : users) {
    userInfo.setIntro("暂无介绍");
    userInfo.setPic("无图片");
    userDao.saveAndFlush(userInfo);
  }*/

  /* List<RecipeKind> recipeKinds = recipeKindDao.findAll();
  for (RecipeKind recipeKind : recipeKinds) {
    if (healthDietDao.getNumberByName(recipeKind.getKindName()) == null) {
      recipeKind.setNormal(1);
    } else recipeKind.setNormal(0);
    recipeKindDao.saveAndFlush(recipeKind);
  }*/

  /* long account = 1000000;
      List<Object[]> recipes = recipeDao.getAllRecipeItem();
      for (Object[] recipe : recipes) {
        long recipeNum = Long.valueOf(recipe[0].toString());
        String userName = recipe[1].toString();
        Long maxAccount = userDao.getMaxAccount();
        long userAccount = maxAccount == null ? account : maxAccount + 1;
        UserInfo user = userDao.getAccountByUserName(userName);
        if (user == null) {
          UserInfo userInfo = new UserInfo();
          userInfo.setUserName(userName);
          userInfo.setUserAccount(userAccount);
          userDao.save(userInfo);
        } else {
          userAccount = user.getUserAccount();
        }
        UserRecipes userRecipes = new UserRecipes();
        userRecipes.setRecipeNum(recipeNum);
        userRecipes.setUserAccount(userAccount);
        userRecipesDao.save(userRecipes);
      }
  */

}
