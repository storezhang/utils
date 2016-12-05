package com.ruijc.mybatis.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Redis工具类
 *
 * @author Storezhang
 */
@Component
@EnableConfigurationProperties(RedisProperties.class)
public class RedisUtils {

    private static RedisTemplate<byte[], byte[]> redisTemplateProxy;
    private static RedisProperties redisPropertiesProxy;

    @Autowired
    private RedisTemplate<byte[], byte[]> redisTemplate;
    @Autowired
    private RedisProperties redisProperties;

    public static RedisTemplate<byte[], byte[]> getRedisTemplate() {
        return RedisUtils.redisTemplateProxy;
    }

    public static RedisProperties getRedisProperties() {
        return RedisUtils.redisPropertiesProxy;
    }

    @PostConstruct
    public void init() {
        RedisUtils.redisTemplateProxy = redisTemplate;
        RedisUtils.redisPropertiesProxy = redisProperties;
    }
}
