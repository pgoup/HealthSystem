package com.application.redis;


/** @author PG */
public interface RecipeRedisService {
  void addCache(String key, String value);

  String queryCache(String key);

  boolean existKey(String key);
}
