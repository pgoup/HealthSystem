package com.application.controller;

import com.application.dao.*;
import com.application.entity.server.*;
import com.application.redis.RecipeRedisService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/** @author PG */
@Controller
public class TestController {

  @Resource private RecipeKindDao recipeKindDao;
  @Resource private RecipeDao recipeDao;
  @Resource private RecipeKindRecipesDao recipeKindRecipesDao;
  @Resource private UserDao userDao;
  @Resource private UserRecipesDao userRecipesDao;
  @Resource private HealthDietDao healthDietDao;

  @ResponseBody
  @RequestMapping("/test")
  public String test() {

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
    return "测试结束";
  }
}
