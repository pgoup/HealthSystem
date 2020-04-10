package com.application.redis.impl;

import com.application.redis.RecipeRedisService;
import javafx.scene.control.TableView;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;

/** @author PG */
@Service
public class RecipeRedisServiceImpl implements RecipeRedisService {

  @Resource private RedisTemplate redisTemplate;

  @Override
  public void addCache(String key, String value) {
    redisTemplate.opsForValue().set(key,value);
  }

  @Override
  public String queryCache(String key) {
    return (String) redisTemplate.opsForValue().get(key);
  }

  @Override
  public boolean existKey(String key) {
    return redisTemplate.hasKey(key);
  }
}
