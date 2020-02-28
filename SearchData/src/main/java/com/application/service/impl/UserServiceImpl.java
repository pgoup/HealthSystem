package com.application.service.impl;

import com.application.dao.*;
import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.UserInfoClient;
import com.application.entity.server.UserCollectRecipes;
import com.application.entity.server.UserConcernedUser;
import com.application.entity.server.UserInfo;
import com.application.service.UserService;
import com.application.util.ConvertUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/** @author PG */
@Service
public class UserServiceImpl implements UserService {
  @Resource private UserDao userDao;
  @Resource private UserCollectRecipesDao collectRecipesDao;
  @Resource private UserRecipesDao userRecipesDao;
  @Resource private RecipeDao recipeDao;
  @Resource private UserConcernedUserDao concernedUserDao;
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

  private UserInfoClient userInfoToInfoClient(UserInfo userInfo) {
    return new UserInfoClient(
        userInfo.getUserAccount(),
        userInfo.getUserName(),
        ConvertUtils.picConvertToByte(userInfo.getPic()),
        userInfo.getIntro(),
        userInfo.getFans(),
        userInfo.getAttentions());
  }
}
