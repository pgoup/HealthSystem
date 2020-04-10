package com.application.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/** @author PG */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
  /** 1.创建 JedisPoolConfig 对象。在该对象中完成一些连接池的配置 */
  @Bean
  @ConfigurationProperties(prefix = "spring.redis.pool")
  public JedisPoolConfig jedisPoolConfig() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    return jedisPoolConfig;
  }

  /** 2.创建 JedisConnectionFactory：配置 redis 连接信息 */
  @Bean
  @ConfigurationProperties(prefix = "spring.redis")
  public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
    JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
    return jedisConnectionFactory;
  }

  /** 3.创建 RedisTemplate:用于执行 Redis 操作的方法 */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      JedisConnectionFactory jedisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    // 关联
    redisTemplate.setConnectionFactory(jedisConnectionFactory);
    // 为 key 设置序列化器
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    // 为 value 设置序列化器
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    return redisTemplate;
  }
}
