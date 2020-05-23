package com.application.service;

import com.application.entity.server.UserAddNutrition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/** @author PG */
@Service
public interface HealthCompute {
  String healthDataCompute(
      float height,
      Map<String, Float> nutritions,
      List<UserAddNutrition> addNutritions,
      Long userAccount);

  String computeImportantNutrition(
      Map<String, Float> nutritions, List<UserAddNutrition> addNutritions);

  String computeTagNutrition(Map<String, Float> nutritions, Long userAccount);
}
