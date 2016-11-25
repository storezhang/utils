/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.mybatis.cache.redis;

import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * 带日志记录的Mybatis缓存的Redis缓存实现
 *
 * @author Storezhang
 */
public class LoggingRedisCache extends LoggingCache {

    public LoggingRedisCache(String id) {
        super(new RedisCache(id));
    }
}
