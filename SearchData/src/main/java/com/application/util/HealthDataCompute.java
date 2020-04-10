package com.application.util;

import java.util.Map;

/** @author PG */
public class HealthDataCompute {

  // 热量
  private static final int HEAT = 20;
  // 蛋白质
  private static final String PROTEIN = "1-1.5";
  // 脂肪
  private static final String FAT = "1-1.2";
  // 碳水化合物
  private static final String CARBOHYDRATE = "400-500";
  // 胆固醇
  private static final String CHOLESTEROL = "50-300";
  // 维生素A-E
  private static final float VITAMINA = 1.1f;
  private static final float VITAMINB1 = 1.4f;
  private static final float VITAMINB2 = 1.6f;
  private static final float VITAMINB6 = 1.6f;
  private static final float VITAMINB12 = 2f;
  private static final float VITAMINC = 60f;
  private static final float VITAMIND = 5f;
  private static final float VITAMINE = 10f;
  // 钾钙钠镁磷铁锌铜锰碘
  private static final float KA = 3f;
  private static final float CA = 600f;
  private static final float NA = 6.2f;
  private static final String MG = "300-350";
  private static final String P = "720-900";
  private static final String FE = "10-12";
  private static final String ZN = "12-16";
  private static final String I = "100-140";

  public static String healthDataCompute(float height, Map<String, Float> nutritions) {
    float heat = height * HEAT;
    float minProtein = Float.valueOf(PROTEIN.split("-")[0]) * height;
    float maxProtein = Float.valueOf(PROTEIN.split("-")[1]) * height;
    float minFat = Float.valueOf(FAT.split("-")[0]) * heat;
    float maxFat = Float.valueOf(FAT.split("-")[1]) * height;
    StringBuilder suggestions = new StringBuilder();
    if (nutritions.get("热量") < heat) suggestions.append("热量略低于建议值，");
    else if (nutritions.get("热量") > heat) suggestions.append("热量略高于建议值，");
    if (nutritions.get("蛋白质") < minProtein) suggestions.append("蛋白质略低于建议值，");
    else if (nutritions.get("蛋白质") > maxProtein) suggestions.append("蛋白质略高于建议值，");
    if (nutritions.get("脂肪") < minFat) suggestions.append("脂肪略低于建议值，");
    else if (nutritions.get("脂肪") > maxFat) suggestions.append("脂肪略高于建议值，");
    if (nutritions.get("碳水化合物") < 400f) suggestions.append("碳水化合物略低于建议值，");
    else if (nutritions.get("碳水化合物") > 500f) suggestions.append("碳水化合物略高于建议值，");
    if (nutritions.get("胆固醇") < 50f) suggestions.append("胆固醇略低于建议值，");
    else if (nutritions.get("胆固醇") > 300f) suggestions.append("胆固醇略高于建议值，");
    if (nutritions.get("维生素A") < 1.1f) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素A") > 1.1f) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素B1") < 1.4f) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素B1") > 1.4f) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素B2") < 1.6f) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素B2") > 1.6f) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素B6") < 1.6f) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素B6") > 1.6f) suggestions.append("维生素A略高于建议值，");
    if (nutritions.get("维生素B12") < 2.0f) suggestions.append("维生素A略低于建议值，");
    else if (nutritions.get("维生素B12") > 2.0f) suggestions.append("维生素A略高于建议值，");

    if (nutritions.get("维生素C") < 60) suggestions.append("维生素C略低于建议值，");
    else if (nutritions.get("维生素C") > 60) suggestions.append("维生素C略高于建议值，");
    if (nutritions.get("维生素D") < 5) suggestions.append("维生素D略低于建议值，");
    else if (nutritions.get("维生素D") > 5) suggestions.append("维生素D略高于建议值，");
    if (nutritions.get("维生素E") < 10) suggestions.append("维生素E略低于建议值，");
    else if (nutritions.get("维生素E") > 10) suggestions.append("维生素E略高于建议值，");
    if (nutritions.get("钾") < 3) suggestions.append("钾略低于建议值，");
    else if (nutritions.get("钾") > 3) suggestions.append("钾略高于建议值，");
    if (nutritions.get("钙") < 600) suggestions.append("钙略低于建议值，");
    else if (nutritions.get("钙") > 600) suggestions.append("钙略高于建议值，");
    if (nutritions.get("钠") < 6.2) suggestions.append("钠略低于建议值，");
    else if (nutritions.get("钠") > 6.2) suggestions.append("钠略高于建议值，");
    if (nutritions.get("镁") < 300) suggestions.append("镁略低于建议值，");
    else if (nutritions.get("镁") > 350) suggestions.append("镁略高于建议值，");
    if (nutritions.get("磷") < 720) suggestions.append("磷略低于建议值，");
    else if (nutritions.get("磷") > 900) suggestions.append("磷略高于建议值，");
    if (nutritions.get("锌") < 12) suggestions.append("锌略低于建议值，");
    else if (nutritions.get("锌") > 16) suggestions.append("锌略高于建议值，");
    if (nutritions.get("铁") < 10) suggestions.append("铁略低于建议值，");
    else if (nutritions.get("铁") > 12) suggestions.append("铁略高于建议值，");
    if (nutritions.get("碘") < 100) suggestions.append("碘略低于建议值，");
    else if (nutritions.get("碘") > 140) suggestions.append("碘略高于建议值，");
    return suggestions.toString();
  }
}
