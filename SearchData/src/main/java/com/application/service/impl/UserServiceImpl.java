package com.application.service.impl;

import com.application.dao.*;
import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.RecipeNutritionSuggestion;
import com.application.entity.client.UserInfoClient;
import com.application.entity.server.UserCollectRecipes;
import com.application.entity.server.UserConcernedUser;
import com.application.entity.server.UserInfo;
import com.application.service.UserService;
import com.application.util.ConvertUtils;
import com.application.util.HealthDataCompute;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;

/** @author PG */
@Service
public class UserServiceImpl implements UserService {
  @Resource private UserDao userDao;
  @Resource private UserCollectRecipesDao collectRecipesDao;
  @Resource private UserRecipesDao userRecipesDao;
  @Resource private RecipeDao recipeDao;
  @Resource private UserConcernedUserDao concernedUserDao;
  @Resource private RecipeNutritionDao recipeNutritionDao;
  private static final String DEFAULT_PIC_PATH = "";

  @Override
  public boolean existUserInfo(Long userAccount) {
    return userDao.getByUserAccount(userAccount) == null ? false : true;
  }

  @Override
  public UserInfoClient getUserInfoByAccountAndPass(Long userAccount, String password) {
    List<Object[]> objects = userDao.getAllByAccountAndPass(Long.valueOf(userAccount), password);
    if (objects == null) return null;
    Object[] result = objects.get(0);
    UserInfoClient client = new UserInfoClient();
    client.setAccount(Long.parseLong(result[0].toString()));
    client.setUserName(result[1].toString());
    client.setPic(result[2] == null ? null : result[2].toString().getBytes());
    client.setIntro(result[3] == null ? "暂无介绍" : result[3].toString());
    client.setFans(result[4] == null ? 0 : Integer.valueOf(result[4].toString()));
    client.setAttentions(result[5] == null ? 0 : Integer.valueOf(result[5].toString()));
    return client;
  }

  @Override
  public void saveUserInfo(Long userAccount, String password) {
    UserInfo userInfo = new UserInfo();
    userInfo.setUserAccount(userAccount);
    userInfo.setPassword(password);
    userInfo.setUserName(String.valueOf(userAccount));
    userInfo.setPic(DEFAULT_PIC_PATH);
    userInfo.setIntro("暂无介绍");
    userInfo.setFans(0);
    userInfo.setAttentions(0);
    System.out.println("保存" + userAccount);
    userDao.save(userInfo);
  }

  @Override
  public void saveUser(UserInfo userInfo) {
    userDao.save(userInfo);
  }

  @Override
  public List<RecipeItemClient> getCollectedRecipesByUserAccount(
      Long userAccount, Pageable pageable) {
    List<Object[]> results =
        collectRecipesDao.getCollectedRecipesByUserAccount(userAccount, pageable);
    return ConvertUtils.recipesConvertToContentItemClient(results);
  }

  @Override
  public List<RecipeItemClient> getMimeRecipes(Long userAccount, Pageable pageable) {
    List<Object[]> results = userRecipesDao.getMimeRecipes(userAccount, pageable);
    return ConvertUtils.recipesConvertToContentItemClient(results);
  }

  @Override
  public boolean deleteCollectedRecipe(Long account, Long recipeNumber) {
    try {
      collectRecipesDao.deleteByAccountAndRecipeNum(account, recipeNumber);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isCollected(Long account, Long recipeNumber) {
    UserCollectRecipes collectRecipes =
        collectRecipesDao.getCollectedRecipes(account, recipeNumber);
    if (collectRecipes != null) {
      collectRecipesDao.delete(collectRecipes);
      recipeDao.updateRecipeCollectedCount(
          recipeNumber, recipeDao.getCollectedCountByRecipeNum(recipeNumber) - 1);
      return true;
    }
    return false;
  }

  @Override
  public boolean addCollectedRecipe(Long account, Long recipeNumber) {
    UserCollectRecipes collectRecipes = new UserCollectRecipes(account, recipeNumber);
    recipeDao.updateRecipeCollectedCount(
        recipeNumber, recipeDao.getCollectedCountByRecipeNum(recipeNumber) + 1);
    try {
      collectRecipesDao.save(collectRecipes);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public UserInfo getUserInfoByAccount(Long userAccount) {
    return userDao.getUserInfoByAccount(userAccount);
  }

  @Override
  public void deleteConcernedAuthor(Long userAccount, Long concernedUserAccount) {
    UserInfo userInfo = userDao.getUserInfoByAccount(userAccount);
    userInfo.setAttentions(userInfo.getAttentions() > 0 ? userInfo.getAttentions() - 1 : 0);
    userDao.save(userInfo);

    UserInfo concernedUserInfo = userDao.getUserInfoByAccount(concernedUserAccount);
    concernedUserInfo.setFans(
        concernedUserInfo.getAttentions() > 0 ? concernedUserInfo.getFans() - 1 : 0);
    userDao.save(concernedUserInfo);
    concernedUserDao.deleteByAccountAndConcernedAccount(userAccount, concernedUserAccount);
  }

  @Override
  public void addConcernedAuthor(Long userAccount, Long concernedUserAccount) {
    UserInfo userInfo = userDao.getUserInfoByAccount(userAccount);
    userInfo.setAttentions(userInfo.getAttentions() + 1);
    userDao.save(userInfo);
    UserInfo concernedUserInfo = userDao.getUserInfoByAccount(concernedUserAccount);
    concernedUserInfo.setFans(concernedUserInfo.getFans() + 1);
    userDao.save(concernedUserInfo);
    UserConcernedUser concernedUser = new UserConcernedUser();
    concernedUser.setUserAccount(userAccount);
    concernedUser.setConcernedUserAccount(concernedUserAccount);
    concernedUserDao.saveAndFlush(concernedUser);
  }

  @Override
  public void updateCollectedRecipe(Long userAccount, Long recipeNum, boolean isCollected) {
    if (isCollected) {
      collectRecipesDao.deleteByAccountAndRecipeNum(userAccount, recipeNum);
    } else {
      UserCollectRecipes collectRecipes = new UserCollectRecipes(userAccount, recipeNum);
      collectRecipesDao.save(collectRecipes);
    }
  }

  @Override
  public boolean existUserConcern(Long userAccount, Long concernedUserAccount) {
    return concernedUserDao.getIdByUserAccountAndConcernedAccount(userAccount, concernedUserAccount)
        != null;
  }

  @Override
  public Long getUserAccountByName(String userName) {
    return userDao.getUserAccountByName(userName);
  }

  @Override
  public String getPicPathByUserAccount(Long userAccount) {
    String path = userDao.getPicPathByUserAccount(userAccount);
    return path == null ? "无图片" : path;
  }

  @Override
  public UserInfoClient getUserInfoClient(Long userAccount) {
    UserInfo userInfo = userDao.getUserInfoByAccount(userAccount);
    return userInfoToInfoClient(userInfo);
  }

  @Override
  public List<UserInfoClient> getConcernedUserInfoClient(Long userAccount, Pageable pageable) {
    List<BigInteger> userAccounts = concernedUserDao.getConcernedUserAccount(userAccount, pageable);
    List<UserInfoClient> infoClients = new ArrayList<>();
    for (int i = 0; i < userAccounts.size(); i++) {
      infoClients.add(
          userInfoToInfoClient(
              userDao.getUserInfoByAccount(Long.parseLong(userAccounts.get(i).toString()))));
    }
    return infoClients;
  }

  @Override
  public List<UserInfoClient> getFanUserInfoClient(Long userAccount, Pageable pageable) {
    List<BigInteger> userAccounts = concernedUserDao.getFanUserAccount(userAccount, pageable);
    List<UserInfoClient> infoClients = new ArrayList<>();
    for (BigInteger account : userAccounts) {
      infoClients.add(userInfoToInfoClient(userDao.getUserInfoByAccount(account.longValue())));
    }
    return infoClients;
  }

  // 保存用户的图片
  @Override
  public boolean uploadUserImage(Long userAccount, MultipartFile file) {
    String imagePath = "C:\\Users\\PG\\Desktop\\HealthRecipe\\userImage\\" + userAccount + ".png";
    try {
      File dest = new File(imagePath);
      file.transferTo(dest);
      userDao.updateUserImage(userAccount, imagePath);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public void uploadUserIntro(Long userAccount, String intro) {
    userDao.updateUserIntro(userAccount, intro);
  }

  @Override
  public UserInfoClient getUserDetailInfo(Long userAccount) {
    return userInfoToInfoClient(userDao.getUserDetailInfo(userAccount));
  }

  @Override
  public void uploadUserInfo(Long userAccount, String infoKind, String data) {
    if (infoKind.equals("userName")) userDao.updateUserName(userAccount, data);
    else if (infoKind.equals("userHeight"))
      userDao.updateUserHeight(userAccount, Float.parseFloat(data));
    else userDao.updateUserWeight(userAccount, Float.parseFloat(data));
  }

  @Override
  public RecipeNutritionSuggestion getRecipesNutritionByRecipeNums(
      Long userAccount, String recipes) {
    float height = userDao.getWeightByAccount(userAccount);
    String[] recipeNums = recipes.split("&&");
    List<String> nutritions = new ArrayList<>();
    for (String recipeNum : recipeNums) {
      nutritions.add(recipeNutritionDao.getNutritionByRecipeNum(Long.valueOf(recipeNum)));
    }
    Map<String, Float> nutritionMap = new LinkedHashMap<>();
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    for (String nutrition : nutritions) {
      String[] values = nutrition.split("##");
      for (String value : values) {
        String[] v = value.split("&&");
        String key = v[0];
        float count = Float.parseFloat(decimalFormat.format(Float.valueOf(v[1].split(" ")[0])));
        if (nutritionMap.containsKey(key)) nutritionMap.put(key, nutritionMap.get(key) + count);
        else nutritionMap.put(key, count);
      }
    }
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
        .append("热量")
        .append("&&")
        .append(nutritionMap.get("热量"))
        .append(" 千卡")
        .append("##")
        .append("胆固醇")
        .append("&&")
        .append(nutritionMap.get("胆固醇"))
        .append(" 毫克")
        .append("##")
        .append("蛋白质")
        .append("&&")
        .append(nutritionMap.get("蛋白质"))
        .append(" 克")
        .append("##")
        .append("碳水化合物")
        .append("&&")
        .append(nutritionMap.get("碳水化合物"))
        .append(" 克")
        .append("##")
        .append("脂肪")
        .append("&&")
        .append(nutritionMap.get("脂肪"))
        .append(" 克")
        .append("##")
        .append("维生素A")
        .append("&&")
        .append(nutritionMap.get("维生素A"))
        .append(" 微克")
        .append("##")
        .append("维生素B1")
        .append("&&")
        .append(nutritionMap.get("维生素B1"))
        .append(" 毫克")
        .append("##")
        .append("维生素B2")
        .append("&&")
        .append(nutritionMap.get("维生素B2"))
        .append(" 毫克")
        .append("##")
        .append("维生素B6")
        .append("&&")
        .append(nutritionMap.get("维生素B6"))
        .append(" 毫克")
        .append("##")
        .append("维生素B12")
        .append("&&")
        .append(nutritionMap.get("维生素B12"))
        .append(" 毫克")
        .append("##")
        .append("维生素C")
        .append("&&")
        .append(nutritionMap.get("维生素C"))
        .append(" 毫克")
        .append("##")
        .append("维生素D")
        .append("&&")
        .append(nutritionMap.get("维生素D"))
        .append(" 毫克")
        .append("##")
        .append("维生素E")
        .append("&&")
        .append(nutritionMap.get("维生素E"))
        .append(" 毫克")
        .append("##")
        .append("钾")
        .append("&&")
        .append(nutritionMap.get("钾"))
        .append(" 毫克")
        .append("##")
        .append("钙")
        .append("&&")
        .append(nutritionMap.get("钙"))
        .append(" 毫克")
        .append("##")
        .append("钠")
        .append("&&")
        .append(nutritionMap.get("钠"))
        .append(" 毫克")
        .append("##")
        .append("镁")
        .append("&&")
        .append(nutritionMap.get("镁"))
        .append(" 毫克")
        .append("##")
        .append("磷")
        .append("&&")
        .append(nutritionMap.get("磷"))
        .append(" 毫克")
        .append("##")
        .append("铁")
        .append("&&")
        .append(nutritionMap.get("铁"))
        .append(" 毫克")
        .append("##")
        .append("锌")
        .append("&&")
        .append(nutritionMap.get("锌"))
        .append(" 毫克")
        .append("##")
        .append("铜")
        .append("&&")
        .append(nutritionMap.get("铜"))
        .append(" 毫克")
        .append("##")
        .append("锰")
        .append("&&")
        .append(nutritionMap.get("锰"))
        .append(" 毫克")
        .append("##")
        .append("碘")
        .append("&&")
        .append(nutritionMap.get("碘"))
        .append(" 毫克")
        .append("##");
    RecipeNutritionSuggestion nutritionSuggestion =
        new RecipeNutritionSuggestion(
            stringBuilder.toString(), HealthDataCompute.healthDataCompute(height, nutritionMap));

    return nutritionSuggestion;
  }

  private UserInfoClient userInfoToInfoClient(UserInfo userInfo) {
    return new UserInfoClient(
        userInfo.getUserAccount(),
        userInfo.getUserName(),
        ConvertUtils.picConvertToByte(userInfo.getPic()),
        userInfo.getIntro(),
        userInfo.getFans(),
        userInfo.getAttentions(),
        userInfo.getHeight(),
        userInfo.getWeight());
  }
}
