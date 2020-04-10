package com.application.controller;

import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.RecipeNutritionSuggestion;
import com.application.entity.client.UserInfoClient;
import com.application.entity.server.UserInfo;
import com.application.service.UserService;
import javafx.geometry.Pos;
import org.apache.bcel.verifier.statics.LONG_Upper;
import org.apache.http.client.HttpRequestRetryHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/** @author PG */
@Controller
public class UserController {
  private static final int PAGE_SIZE = 10;

  @Resource private UserService userService;

  /**
   * 保存用户信息
   *
   * @param request
   */
  @RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
  @ResponseBody
  public void saveUserInfo(HttpServletRequest request) {
    String account = request.getParameter("account");
    String password = request.getParameter("password");
    userService.saveUserInfo(Long.valueOf(account), password);
  }

  /**
   * 获取用户信息
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
  @ResponseBody
  public UserInfoClient getUserInfo(HttpServletRequest request) {
    Long account = Long.parseLong(request.getParameter("account"));
    String password = request.getParameter("password");
    return userService.getUserInfoByAccountAndPass(account, password);
  }

  /**
   * 是否存在用户信息
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/existUserInfo", method = RequestMethod.POST)
  @ResponseBody
  public Boolean existUserInfo(HttpServletRequest request) {
    Long account = Long.valueOf(request.getParameter("account"));
    return userService.existUserInfo(account);
  }

  /** 获取用户的收集食谱 */
  @RequestMapping(value = "/getCollectedRecipesByUserAccount", method = RequestMethod.POST)
  @ResponseBody
  public List<RecipeItemClient> getCollectedRecipesByUserAccount(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    int pageCount = Integer.valueOf(request.getParameter("pageCount"));
    Sort sort = new Sort(Sort.Direction.DESC, "recipe_id");
    Pageable pageable = new PageRequest(pageCount -1, PAGE_SIZE, sort);
    return userService.getCollectedRecipesByUserAccount(userAccount, pageable);
  }

  @RequestMapping(value = "/getMimeRecipesByUserAccount", method = RequestMethod.POST)
  @ResponseBody
  public List<RecipeItemClient> getMimeRecipesByUserAccount(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    int pageCount = Integer.valueOf(request.getParameter("pageCount"));
    Sort sort = new Sort(Sort.Direction.DESC, "recipe_id");
    Pageable pageable = new PageRequest(pageCount - 1, PAGE_SIZE, sort);
    List<RecipeItemClient> clients = userService.getMimeRecipes(userAccount, pageable);
    return clients;
  }

  /**
   * 是否被收藏
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/isCollected", method = RequestMethod.POST)
  @ResponseBody
  public boolean isCollected(HttpServletRequest request) {
    Long account = Long.parseLong(request.getParameter("userAccount"));
    if (account == null) {
      return false;
    }
    long recipeNumber = Long.valueOf(request.getParameter("recipeNum"));
    return userService.isCollected(account, recipeNumber);
  }

  /**
   * 收藏食谱
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/collectRecipe", method = RequestMethod.POST)
  @ResponseBody
  public boolean collectedRecipe(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    Long recipeNumber = Long.parseLong(request.getParameter("recipeNum"));
    return userService.addCollectedRecipe(userAccount, recipeNumber);
  }

  /**
   * 取消收藏食谱
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/deleteCollectedRecipe", method = RequestMethod.POST)
  @ResponseBody
  public boolean deleteCollectedRecipe(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    Long recipeNumber = Long.parseLong(request.getParameter("recipeNum"));
    return userService.deleteCollectedRecipe(userAccount, recipeNumber);
  }

  @RequestMapping(value = "/deleteUserTags", method = RequestMethod.POST)
  @ResponseBody
  private boolean deleteUserTags(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    String tag = request.getParameter("tag");
    UserInfo userInfo = userService.getUserInfoByAccount(userAccount);
    String tags = userInfo.getTags();

    String[] values = tags.split("&&");
    StringBuilder newTags = new StringBuilder();
    for (String value : values) {
      if (!value.equals(tag)) newTags.append(value).append("&&");
    }
    userInfo.setTags(newTags.toString());
    userService.saveUser(userInfo);
    return true;
  }

  @RequestMapping(value = "/addUserTags", method = RequestMethod.POST)
  @ResponseBody
  private void addUserTags(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    String tags = request.getParameter("tags");
    UserInfo userInfo = userService.getUserInfoByAccount(userAccount);
    String[] values = userInfo.getTags().split("&&");
    String[] tagValues = tags.split("&&");
    Set<String> tagSet = new HashSet<>();
    for (String value : values) {
      tagSet.add(value);
    }
    for (String value : tagValues) {
      tagSet.add(value);
    }
    Iterator iterator = tagSet.iterator();
    StringBuilder builder = new StringBuilder();
    while (iterator.hasNext()) {
      builder.append(iterator.next()).append("&&");
    }

    userInfo.setTags(builder.toString());
    userService.saveUser(userInfo);
  }

  @RequestMapping(value = "/getUserTags", method = RequestMethod.POST)
  @ResponseBody
  private List<String> getUserTags(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    UserInfo userInfo = userService.getUserInfoByAccount(userAccount);
    String tags = userInfo.getTags();
    if (tags.isEmpty()) return new ArrayList<>();
    String[] values = userInfo.getTags().split("&&");
    List<String> result = new ArrayList<>();
    for (String value : values) {
      result.add(value);
    }
    return result;
  }

  @RequestMapping(value = "/updateUserConcern", method = RequestMethod.POST)
  @ResponseBody
  private boolean updateUserConcern(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    Long concernedUserAccount = Long.parseLong(request.getParameter("concernedUserAccount"));
    boolean isConcerned = request.getParameter("isConcerned").equals("true");
    if (userAccount.equals(concernedUserAccount)) return false;
    if (isConcerned) userService.deleteConcernedAuthor(userAccount, concernedUserAccount);
    else userService.addConcernedAuthor(userAccount, concernedUserAccount);
    return true;
  }

  @ResponseBody
  @RequestMapping(value = "/updateUserCollect", method = RequestMethod.POST)
  private boolean updateUserCollect(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    Long collectRecipeNum = Long.parseLong(request.getParameter("collectRecipeNum"));
    boolean isCollected = request.getParameter("isCollected").equals("true");
    System.out.println(
        "账号：" + userAccount + " num:" + collectRecipeNum + "  iscollected " + isCollected);
    if (userAccount == null || collectRecipeNum == null) return false;
    else userService.updateCollectedRecipe(userAccount, collectRecipeNum, isCollected);
    return true;
  }

  @RequestMapping(value = "/existUserConcern", method = RequestMethod.POST)
  @ResponseBody
  private boolean existUserConcern(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    Long concernedUserAccount = Long.parseLong(request.getParameter("concernedUserAccount"));
    return userService.existUserConcern(userAccount, concernedUserAccount);
  }

  @RequestMapping(value = "/getUserInfoClient", method = RequestMethod.POST)
  @ResponseBody
  private UserInfoClient getUserInfoClient(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    return userService.getUserInfoClient(userAccount);
  }

  @RequestMapping(value = "/getUserConcernedFans", method = RequestMethod.POST)
  @ResponseBody
  private List<UserInfoClient> getUserConcernedFans(HttpServletRequest request) {
    int pageCount = Integer.parseInt(request.getParameter("pageCount"));
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    String kind = request.getParameter("kind");
    Sort sort = new Sort(Sort.Direction.DESC, "id");
    Pageable pageable = new PageRequest(pageCount, 10, sort);
    if (kind.equals("concern"))
      return userService.getConcernedUserInfoClient(userAccount, pageable);
    else return userService.getFanUserInfoClient(userAccount, pageable);
  }

  @RequestMapping(value = "/uploadUserImage", method = RequestMethod.POST)
  @ResponseBody
  private boolean uploadUserImage(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    MultipartFile file = ((MultipartHttpServletRequest) request).getFile("num0");
    System.out.println(file == null);
    return userService.uploadUserImage(userAccount, file);
  }

  @RequestMapping(value = "/uploadIntro", method = RequestMethod.POST)
  @ResponseBody
  private void uploadIntro(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    String intro = request.getParameter("intro");
    userService.uploadUserIntro(userAccount, intro);
  }

  @RequestMapping(value = "/getUserDetailInfo", method = RequestMethod.POST)
  @ResponseBody
  private UserInfoClient getUserDetailInfo(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    return userService.getUserDetailInfo(userAccount);
  }

  @RequestMapping(value = "/uploadUserInfo", method = RequestMethod.POST)
  @ResponseBody
  private void uploadUserInfo(HttpServletRequest request) {
    Long userAccount = Long.parseLong(request.getParameter("userAccount"));
    String updateKind = request.getParameter("updateKind");
    String data = request.getParameter("data");
    userService.uploadUserInfo(userAccount, updateKind, data);
  }

  // 根据用户提供的食谱以及用户自身的身高体重等信息计算营养参数值
  @ResponseBody
  @RequestMapping(value = "/getRecipesNutritionByRecipeNums", method = RequestMethod.POST)
  private RecipeNutritionSuggestion getRecipesNutritionByRecipeNums(HttpServletRequest request) {
    Long userAccount = Long.valueOf(request.getParameter("userAccount"));
    String recipes = request.getParameter("recipes");
    return userService.getRecipesNutritionByRecipeNums(userAccount, recipes);
  }
}
