package com.example.delivery.global.config;

import com.example.delivery.domain.order.dto.CartItemDTO;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

  //  @Bean
  //  public RedisTemplate<String, Object> redisTemplate() {
  //    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
  //
  //    // Redis를 연결합니다.
  //    redisTemplate.setConnectionFactory(redisConnectionFactory());
  //
  //    // Key-Value 형태로 직렬화를 수행합니다.
  //    redisTemplate.setKeySerializer(new StringRedisSerializer());
  //    redisTemplate.setValueSerializer(new StringRedisSerializer());
  //
  //    // Hash Key-Value 형태로 직렬화를 수행합니다.
  //    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
  //    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
  //
  //    // 기본적으로 직렬화를 수행합니다.
  //    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
  //
  //    return redisTemplate;
  //  }

  @Bean
  @Qualifier("deliveryRedisTemplate")
  public RedisTemplate<String, Object> deliveryRedisTemplate() {
    GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer =
        new GenericJackson2JsonRedisSerializer();

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
    redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

    return redisTemplate;
  }

  @Bean
  @Qualifier("cartItemDTORedisTemplate")
  public RedisTemplate<String, CartItemDTO> cartItemRedisTemplate() {
    GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer =
        new GenericJackson2JsonRedisSerializer();

    RedisTemplate<String, CartItemDTO> redisTemplate = new RedisTemplate<>();

    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);

    return redisTemplate;
  }

  @Bean
  public RedisCacheManager redisCacheManager() {

    RedisCacheConfiguration redisCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()))
            .entryTtl(Duration.ofDays(1L));

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
            redisConnectionFactory())
        .cacheDefaults(redisCacheConfiguration)
        .build();
  }
}
