package com.application.controller;

import com.application.entity.client.RecipeItemClient;
import com.application.entity.client.HealthDietClient;
import com.application.service.HealthDietService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/** @author PG */
@Controller
public class HealthDietController {

  @Resource private HealthDietService healthDietService;

  /** 获取所有一级健康饮食类型 */
  @PostMapping("/getAllHealthDietKind")
  @ResponseBody
  private List<String> getAllHealthDietKind() {
    return healthDietService.getAllHealthDietKind();
  }

  @RequestMapping(value = "/getAllClassifies", method = RequestMethod.POST)
  @ResponseBody
  private Map<String, List<String>> getAllClassifies(HttpServletRequest request) {
    return healthDietService.getAllClassifies();
  }

  /** 根据一级健康饮食类型获取具体的饮食对象 */
  @PostMapping("/getHealthDietNameByDietKind")
  @ResponseBody
  public List<String> getHealthDietNameByDietKind(HttpServletRequest request) {
    String healthDietKind = request.getParameter("healthDietKind");
    return healthDietService.getHealthDietNameByDietKind(healthDietKind);
  }

  /** 根据二级分类获取相对应的所有食谱 */
  @PostMapping("/getRecipesByDietName")
  @ResponseBody
  public List<RecipeItemClient> getRecipesByDietName(HttpServletRequest request) {
    String dietName = request.getParameter("healthDietName");
    return healthDietService.getRecipesByDietName(dietName);
  }

  /** 根据二级分类的名称获取相应的信息 */
  @PostMapping("/getDietByHealthDietName")
  @ResponseBody
  public HealthDietClient getDietByHealthDietName(HttpServletRequest request) {
    String dietName = request.getParameter("healthDietName");
    return healthDietService.getDietByHealthDietName(dietName);
  }
}
