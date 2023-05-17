package com.cwc.phonebook.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfiguration {
    @Value("${redis.host}")
    private String redisHost;// = "redis-12168.c301.ap-south-1-1.ec2.cloud.redislabs.com";

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.password}")
    private String redisPassword;

    @Value("${redis.username}")
    private String redisUsername;
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost,redisPort);
        configuration.setHostName(redisHost);
        configuration.setUsername(redisUsername);
        configuration.setPassword(redisPassword);
        configuration.setPort(redisPort);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisCacheManager redisCacheManager(){
        RedisCacheConfiguration cacheConfiguration = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfiguration)
                .withCacheConfiguration("phonebooks", myDefaultCacheConfig(Duration.ofMinutes(5)))
                .withCacheConfiguration("phonebook", myDefaultCacheConfig(Duration.ofMinutes(1)))
                .build();
    }

    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
