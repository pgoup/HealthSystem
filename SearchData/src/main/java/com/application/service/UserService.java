package com.application.service;

import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.RecipeNutritionSuggestion;
import com.application.entity.client.UserInfoClient;
import com.application.entity.server.UserAddNutrition;
import com.application.entity.server.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
  boolean existUserInfo(Long userAccount);

  UserInfoClient getUserInfoByAccountAndPass(Long userAccount, String password);

  boolean saveUserInfo(Long userAccount, String password);

  void saveUser(UserInfo userInfo);

  Long getUserAccountByName(String userName);
  /** 获取用户收藏的食谱 */
  List<RecipeItemClient> getCollectedRecipesByUserAccount(Long userAccount, Pageable pageable);

  /** 获取用户自己制作的食谱 */
  List<RecipeItemClient> getMimeRecipes(Long userAccount, Pageable pageable);

  boolean addCollectedRecipe(Long userAccount, Long recipeNumber);

  boolean deleteCollectedRecipe(Long userAccount, Long recipeNumber);

  boolean isCollected(Long userAccount, Long recipeNumber);
  // 添加我的食谱未完善
  // boolean addMimeRecipe(String account, Long recipeNumber);

  UserInfo getUserInfoByAccount(Long userAccount);

  boolean deleteConcernedAuthor(Long userAccount, Long concernedUserAccount);

  void addConcernedAuthor(Long userAccount, Long concernedUserAccount);

  void updateCollectedRecipe(Long userAccount, Long recipeNum, boolean isCollected);

  boolean existUserConcern(Long userAccount, Long concernedUserAccount);

  String getPicPathByUserAccount(Long userAccount);

  UserInfoClient getUserInfoClient(Long userAccount);

  // 获取关注的用户
  List<UserInfoClient> getConcernedUserInfoClient(Long userAccount, Pageable pageable);

  // 获取粉丝
  List<UserInfoClient> getFanUserInfoClient(Long userAccount, Pageable pageable);

  boolean uploadUserImage(Long userAccount, MultipartFile file);

  void uploadUserIntro(Long userAccount, String intro);

  UserInfoClient getUserDetailInfo(Long userAccount);

  void uploadUserInfo(Long userAccount, String infoKind, String data);

  RecipeNutritionSuggestion getRecipesNutritionByRecipeNums(Long userAccount, List<String> recipes);

  boolean saveUserNutrition(String userAccount, String name, float min, float max);

  List<UserAddNutrition> getUserAddNutritions(String userAccount);

  boolean deleteUserAddNutrition(String userAccount,String nutritionName);
}
