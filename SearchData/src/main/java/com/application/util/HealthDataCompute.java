package com.application.util;

import com.application.entity.server.UserAddNutrition;

import java.util.List;
import java.util.Map;

/** @author PG */
public class HealthDataCompute {

  // 热量
  private static final int HEAT = 20;
  // 蛋白质
  private static final float MIN_PROTEIN = 1f;
  private static final float MAX_PROTEIN = 1.5f;
  // 脂肪
  private static final float MIN_FAT = 1f;
  private static final float MAX_FAT = 1.2f;
  // 碳水化合物
  private static final float MIN_CARBOHYDRATE = 400f;
  private static final float MAX_CARBOHYDRATE = 500f;
  // 胆固醇
  private static final float MIN_CHOLESTEROL = 50f;
  private static final float MAX_CHOLESTEROL = 300f;
  // 维生素A-E
  private static final float VITAMIN_A = 1.1f;
  private static final float VITAMIN_B1 = 1.4f;
  private static final float VITAMIN_B2 = 1.6f;
  private static final float VITAMIN_B6 = 1.6f;
  private static final float VITAMIN_B12 = 2f;
  private static final float VITAMIN_C = 60f;
  private static final float VITAMIN_D = 5f;
  private static final float VITAMIN_E = 10f;
  // 钾钙钠镁磷铁锌铜锰碘
  private static final float KA = 3f;
  private static final float CA = 600f;
  private static final float NA = 6.2f;
  private static final float MIN_MG = 300f;
  private static final float MAX_MG = 350f;
  private static final float MIN_P = 720f;
  private static final float MAX_P = 900f;
  private static final float MIN_FE = 10f;
  private static final float MAX_FE = 12f;
  private static final float MIN_ZN = 12f;
  private static final float MAX_ZN = 16f;
  private static final float MIN_I = 100f;
  private static final float MAX_I = 140f;

  /*public static String healthDataCompute(
      float height,
      Map<String, Float> nutritions,
      List<UserAddNutrition> addNutritions,
      Long userAccount) {
    float heat = height * HEAT;
    float minProtein = MIN_PROTEIN * height;
    float maxProtein = MAX_PROTEIN * height;
    float minFat = MIN_FAT * heat;
    float maxFat = MAX_FAT * height;
    StringBuilder suggestions = new StringBuilder();
    if (nutritions.get("热量") < heat) suggestions.append("热量略低于建议值，");
    else if (nutritions.get("热量") > heat) suggestions.append("热量略高于建议值，");
    if (nutritions.get("蛋白质") < minProtein) suggestions.append("蛋白质略低于建议值，");
    else if (nutritions.get("蛋白质") > maxProtein) suggestions.append("蛋白质略高于建议值，");
    if (nutritions.get("脂肪") < minFat) suggestions.append("脂肪略低于建议值，");
    else if (nutritions.get("脂肪") > maxFat) suggestions.append("脂肪略高于建议值，");
    if (nutritions.get("碳水化合物") < MIN_CARBOHYDRATE) suggestions.append("碳水化合物略低于建议值，");
    else if (nutritions.get("碳水化合物") > MAX_CARBOHYDRATE) suggestions.append("碳水化合物略高于建议值，");
    if (nutritions.get("胆固醇") < MIN_CHOLESTEROL) suggestions.append("胆固醇略低于建议值，");
    else if (nutritions.get("胆固醇") > MAX_CHOLESTEROL) suggestions.append("胆固醇略高于建议值，");
    if (nutritions.get("维生素A") < VITAMIN_A) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素A") > VITAMIN_A) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素B1") < VITAMIN_B1) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素B1") > VITAMIN_B1) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素B2") < VITAMIN_B2) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素B2") > VITAMIN_B2) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素B6") < VITAMIN_B6) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素B6") > VITAMIN_B6) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素B12") < VITAMIN_B12) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素B12") > VITAMIN_B12) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素C") < VITAMIN_C) suggestions.append("维生素C略低于建议值，");
    else if (nutritions.get("维生素C") > VITAMIN_C) suggestions.append("维生素C略高于建议值，");
    if (nutritions.get("维生素D") < VITAMIN_D) suggestions.append("维生素D略低于建议值，");
    else if (nutritions.get("维生素D") > VITAMIN_D) suggestions.append("维生素D略高于建议值，");
    if (nutritions.get("维生素E") < VITAMIN_E) suggestions.append("维生素E略低于建议值，");
    else if (nutritions.get("维生素E") > VITAMIN_E) suggestions.append("维生素E略高于建议值，");
    if (nutritions.get("钾") < KA) suggestions.append("钾略低于建议值，");
    else if (nutritions.get("钾") > KA) suggestions.append("钾略高于建议值，");
    if (nutritions.get("钙") < CA) suggestions.append("钙略低于建议值，");
    else if (nutritions.get("钙") > CA) suggestions.append("钙略高于建议值，");
    if (nutritions.get("钠") < NA) suggestions.append("钠略低于建议值，");
    else if (nutritions.get("钠") > NA) suggestions.append("钠略高于建议值，");
    if (nutritions.get("镁") < MIN_MG) suggestions.append("镁略低于建议值，");
    else if (nutritions.get("镁") > MAX_MG) suggestions.append("镁略高于建议值，");
    if (nutritions.get("磷") < MIN_P) suggestions.append("磷略低于建议值，");
    else if (nutritions.get("磷") > MAX_P) suggestions.append("磷略高于建议值，");
    if (nutritions.get("锌") < MIN_ZN) suggestions.append("锌略低于建议值，");
    else if (nutritions.get("锌") > MAX_ZN) suggestions.append("锌略高于建议值，");
    if (nutritions.get("铁") < MIN_FE) suggestions.append("铁略低于建议值，");
    else if (nutritions.get("铁") > MAX_FE) suggestions.append("铁略高于建议值，");
    if (nutritions.get("碘") < MIN_I) suggestions.append("碘略低于建议值，");
    else if (nutritions.get("碘") > MAX_I) suggestions.append("碘略高于建议值，");
    suggestions.append("&& ").append(computeImportantNutrition(nutritions, addNutritions));
    suggestions.append("&& ").append(computeTagNutrition(nutritions, userAccount.toString()));
    return suggestions.toString();
  }

  public static String computeImportantNutrition(
      Map<String, Float> nutritions, List<UserAddNutrition> addNutritions) {
    StringBuilder warning = new StringBuilder();
    for (UserAddNutrition nutrition : addNutritions) {
      if (nutritions.get(nutrition.getNutritionName()) < nutrition.getMinCount())
        warning.append(nutrition.getNutritionName() + "低于指标，");
      if (nutritions.get(nutrition.getNutritionName()) > nutrition.getMaxCount())
        warning.append(nutrition.getNutritionName() + "高于指标值；");
    }
    return warning.toString();
  }

  public static String computeTagNutrition(Map<String, Float> nutritions) {
    StringBuilder stringBuilder = new StringBuilder();


    return "";
  }*/
}
