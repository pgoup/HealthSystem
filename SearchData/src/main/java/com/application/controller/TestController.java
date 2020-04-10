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

  private void saveNutrition(Long foodNum, Map<String, Float> nutrition) {

    // 热量
    /*float heat = nutrition.containsKey("热量") ? nutrition.get("热量") : 0;

    // 胆固醇
    float cholesterol = nutrition.containsKey("胆固醇") ? nutrition.get("胆固醇") : 0;
    // 蛋白质
    float protein = nutrition.containsKey("蛋白质") ? nutrition.get("蛋白质") : 0;
    // 碳水化合物
    float carbohydrate = nutrition.containsKey("碳水化合物") ? nutrition.get("碳水化合物") : 0;
    // 脂肪
    float fat = nutrition.containsKey("脂肪") ? nutrition.get("脂肪") : 0;
    // 维生素A-E*/
    // float vitaminA = nutrition.containsKey("维生素A") ? nutrition.get("维生素A") : 0;
    float vitaminB1 = nutrition.containsKey("维生素B1") ? nutrition.get("维生素B1") : 0;
    float vitaminB2 = nutrition.containsKey("维生素B2") ? nutrition.get("维生素B2") : 0;
    float vitaminB6 = nutrition.containsKey("维生素B6") ? nutrition.get("维生素B6") : 0;
    float vitaminB12 = nutrition.containsKey("维生素B12") ? nutrition.get("维生素B12") : 0;
    nutritionsDao.updateVitimB(vitaminB1, vitaminB2, vitaminB6, vitaminB12, foodNum);
    // 钾钙钠镁磷铁锌铜锰碘
    /*  float Ka = nutrition.containsKey("钾") ? nutrition.get("钾") : 0;
    float Ca = nutrition.containsKey("钙") ? nutrition.get("钙") : 0;
    float Na = nutrition.containsKey("钠") ? nutrition.get("钠") : 0;
    float Mg = nutrition.containsKey("镁") ? nutrition.get("镁") : 0;
    float P = nutrition.containsKey("磷") ? nutrition.get("磷") : 0;
    float Fe = nutrition.containsKey("铁") ? nutrition.get("铁") : 0;
    float Zn = nutrition.containsKey("锌") ? nutrition.get("锌") : 0;
    float Cu = nutrition.containsKey("铜") ? nutrition.get("铜") : 0;
    float Mn = nutrition.containsKey("锰") ? nutrition.get("锰") : 0;
    float I = nutrition.containsKey("碘") ? nutrition.get("碘") : 0;
    Nutritions nutritions =
        new Nutritions(
            foodId,
            heat,
            cholesterol,
            protein,
            carbohydrate,
            fat,
            vitaminA,
            vitaminB,
            vitaminC,
            vitaminD,
            vitaminE,
            Ka,
            Ca,
            Na,
            Mg,
            P,
            Fe,
            Zn,
            Cu,
            Mn,
            I);*/
    /// nutritionsDao.save(nutritions);
  }

  @ResponseBody
  @RequestMapping("/test1")
  public String test1() {
    List<Food> foods = foodDao.findAll();
    for (Food food : foods) {
      foodNutritionDao.updateFoodNum(food.getFoodName(), food.getNumber());
    }
    return "测试完毕";
  }

  @ResponseBody
  @RequestMapping("/test")
  public String test() throws Exception {
    List<Food> foods = foodDao.findAll();
    for (Food food : foods) {
      String ingredient = food.getNutritionalIngredient().replace("/", "##");
      foodDao.updateIngredient(food.getNumber(), ingredient);
    }
    return "success";
  }

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
