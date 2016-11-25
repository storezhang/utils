package com.ruijc.mybatis.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Redis工具类
 *
 * @author Storezhang
 */
@Component
public class RedisUtils {

    private static RedisTemplate<byte[], byte[]> proxy;

    @Autowired
    private RedisTemplate<byte[], byte[]> self;

    public static RedisTemplate<byte[], byte[]> getRedisTemplate() {
        return proxy;
    }

    @PostConstruct
    public void init() {
        proxy = self;
    }
}
