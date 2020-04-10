package com.application.controller;

import com.alibaba.fastjson.JSONObject;
import com.application.dao.RecipeKindRecipesDao;
import com.application.dao.UserRecipesDao;
import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.RecipeClient;
import com.application.entity.server.Recipe;
import com.application.entity.server.UserInfo;
import com.application.entity.server.UserRecipes;
import com.application.redis.RecipeRedisService;
import com.application.service.HealthDietService;
import com.application.service.RecipeClientService;
import com.application.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/** @author PG */
@Controller
public class RecipeController {

  @Resource private RecipeClientService recipeClientService;
  @Resource private RecipeRedisService recipeRedisService;
  @Resource private RecipeKindRecipesDao recipeKindRecipesDao;
  @Resource private UserService userService;
  @Resource private UserRecipesDao userRecipesDao;
  @Resource private HealthDietService healthDietService;

  /** 根据菜单类型查找食谱 */
  @RequestMapping(value = "/getRecipesByRecipeKind", method = RequestMethod.POST)
  @ResponseBody
  private List<RecipeItemClient> getRecipesByRecipeKind(HttpServletRequest request) {
    String recipeKind = request.getParameter("kindName");
    String pageCount = request.getParameter("pageCount");
    String pageSize = request.getParameter("pageSize");
    Sort sort = new Sort(Sort.Direction.DESC, "recipe.recipe_id");

    Pageable pageable =
        new PageRequest(Integer.parseInt(pageCount) - 1, Integer.parseInt(pageSize), sort);
    List<RecipeItemClient> results =
        recipeClientService.getRecipesByRecipeKind(recipeKind, pageable);
    List<RecipeItemClient> clients = new ArrayList<>();
    for (int i = 0; i < results.size(); i++) {
      clients.add(results.get(i));
    }
    return clients;
  }

  /** 根据健康饮食类型查找食谱 */
  @RequestMapping(value = "/getRecipesByHealthDiet")
  @ResponseBody
  private List<RecipeItemClient> getRecipesByHealthDiet(HttpServletRequest request) {
    String healthDietName = request.getParameter("healthDietName");
    String pageCount = request.getParameter("pageCount");
    String pageSize = request.getParameter("pageSize");
    Sort sort = new Sort(Sort.Direction.DESC, "recipe.recipe_id");
    Pageable pageable =
        new PageRequest(Integer.parseInt(pageCount) - 1, Integer.parseInt(pageSize), sort);
    List<RecipeItemClient> clients =
        recipeClientService.getRecipesByHealthDiet(healthDietName, pageable);
    return clients;
  }

  /** 根据功能性好处查找食谱 */
  @RequestMapping(value = "/getRecipesByFunctionBenefit")
  @ResponseBody
  private List<RecipeItemClient> getRecipesByFunctionBenefit(HttpServletRequest request) {
    String benefitName = request.getParameter("benefitName");
    String pageCount = request.getParameter("pageCount");
    String pageSize = request.getParameter("pageSize");
    Sort sort = new Sort(Sort.Direction.DESC, "recipe.recipe_id");

    Pageable pageable =
        new PageRequest(Integer.parseInt(pageCount) - 1, Integer.parseInt(pageSize), sort);
    List<RecipeItemClient> clients =
        recipeClientService.getRecipesByFunctionBenefit(benefitName, pageable);

    return clients;
  }

  /** 根据食谱名称查找具体的某个食谱 */
  @RequestMapping(value = "/getRecipeByNum", method = RequestMethod.POST)
  @ResponseBody
  private RecipeClient getRecipeByNum(HttpServletRequest request) {
    String recipeNum = request.getParameter("recipeNum");
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    RecipeClient recipeClient = recipeClientService.getRecipeByNum(recipeNum, userAccount);
    System.out.println(
        "食谱的坐着编号为：" + recipeClient.getAuthorAccount() + "是否关注：" + recipeClient.isConcerned());
    return recipeClient;
  }

  /** 根据类别随机获取十个数据 */
  @RequestMapping(value = "/getRandomRecipesByRecipeKind", method = RequestMethod.POST)
  @ResponseBody
  private List<RecipeItemClient> getRandomRecipesByRecipeKind(HttpServletRequest request) {
    String kind = request.getParameter("kind");
    return recipeClientService.getRandomRecipesByRecipeKind(kind);
  }

  @RequestMapping(value = "/updateData", method = RequestMethod.GET)
  @ResponseBody
  private String updateData() {
    String kind = "";
    recipeClientService.updateData();
    return "数据更新成功";
  }

  @RequestMapping(value = "/getRecipesByKeyWord")
  @ResponseBody
  private List<RecipeItemClient> getRecipesByKeyWord(HttpServletRequest request) {
    String keyWord = request.getParameter("keyWord");
    int pageSize = 10;
    int pageCount = Integer.valueOf(request.getParameter("pageCount"));
    Sort sort = new Sort(Sort.Direction.DESC, "recipe.recipe_id");

    Pageable pageable = new PageRequest(pageCount - 1, pageSize, sort);
    List<RecipeItemClient> clients = recipeClientService.getRecipesByKeyWord(keyWord, pageable);
    if (clients == null) clients = new ArrayList<>();
    return clients;
  }

  @RequestMapping(value = "/uploadRecipe", method = RequestMethod.POST)
  @ResponseBody
  private boolean uploadRecipe(HttpServletRequest request) {
    int imageCount = Integer.parseInt(request.getParameter("imageCount"));
    Recipe recipe = new Recipe();
    long num = recipeClientService.getMaxRecipeNum() + 1;
    // System.out.println("新食谱的编号为：" + num);
    recipe.setRecipeNum(num);
    recipe.setRecipeName(request.getParameter("recipeName"));
    recipe.setKind(request.getParameter("recipeKind"));
    recipe.setMainIngredient(request.getParameter("mainIngredient"));
    recipe.setAccessories(request.getParameter("accessory"));
    String[] values = recipe.getMainIngredient().split("&&");
    recipe.setKeyIngredient(values.length > 0 ? values[0] : "");
    recipe.setAuthor(request.getParameter("author"));
    recipe.setCollectCount(0);
    recipe.setViewCount(0);
    String relativePath = "C:\\Users\\PG\\Desktop\\HealthRecipe\\recipe\\";

    List<String> measureImagePaths = new ArrayList<>();
    for (int i = 0; i < imageCount; i++) {
      String key = "num" + i;
      MultipartFile file = ((MultipartHttpServletRequest) request).getFile(key);
      String path = relativePath + "recipe" + num + ".png";

      if (i == imageCount - 1) {
        recipe.setPicPath(path);

      } else {
        String measureImagePath =
            relativePath + "\\measure\\" + num + "\\" + num + "_" + (i + 1) + ".png";
        String measureRelativePath = relativePath + "\\measure\\" + num;
        File measurePath = new File(measureRelativePath);
        if (!measurePath.exists()) measurePath.mkdir();
        measureImagePaths.add(measureImagePath);
        path = measureImagePath;
      }

      try {
        File dest = new File(path);
        file.transferTo(dest);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    StringBuilder measures = new StringBuilder();
    List<String> measureItems =
        JSONObject.parseArray(request.getParameter("measure"), String.class);
    for (int i = 0; i < measureItems.size(); i++) {
      measures.append(measureItems.get(i));
      measures.append("&&");
      measures.append(measureImagePaths.get(i));
      measures.append("##");
    }
    recipe.setMeasure(measures.toString());

    long userAccount = userService.getUserAccountByName(recipe.getAuthor());
    UserRecipes userRecipes = new UserRecipes(num, userAccount);
    userRecipesDao.save(userRecipes);
    recipeClientService.saveRecipe(recipe);
    return true;
  }

  @RequestMapping(value = "/getAllRecipeKind", method = RequestMethod.POST)
  @ResponseBody
  private List<String> getAllRecipeKind(HttpServletRequest request) {
    return recipeClientService.getAllRecipeKind();
  }

  @RequestMapping(value = "/getAllTags", method = RequestMethod.POST)
  @ResponseBody
  private Map<String, List<String>> getAllTags(HttpServletRequest request) {
    Map<String, List<String>> map = new HashMap<>();
    String firstKind = "菜类";
    List<String> firstList = recipeClientService.getAllRecipeKind();
    map.put(firstKind, firstList);
    List<String> secondList = healthDietService.getAllHealthDietKind();
    for (String value : secondList) {
      map.put(value, healthDietService.getHealthDietNameByDietKind(value));
    }
    return map;
  }

  // 首页根据用户的标签推荐食谱
  @RequestMapping(value = "/getHomePageRecommendedRecipe", method = RequestMethod.POST)
  @ResponseBody
  private List<RecipeItemClient> getHomePageRecommendedRecipe(HttpServletRequest request) {
    String userAccount = request.getParameter("userAccount");
    int pageCount = Integer.parseInt(request.getParameter("pageCount"));
    String kind = "";
    if (!userAccount.isEmpty()) {
      kind = userService.getUserInfoByAccount(Long.parseLong(userAccount)).getTags();
    }
    System.out.println("种类是否为空：" + kind.isEmpty());
    if (kind.isEmpty()) {
      return recipeClientService.getRandomRecipesByRecipeKind("家常菜");
    } else {
      return recipeClientService.getRandomRecipesByUserTags(kind);
    }
  }
}
