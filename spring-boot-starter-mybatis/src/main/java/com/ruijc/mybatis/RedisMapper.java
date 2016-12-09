package com.ruijc.mybatis;

import com.ruijc.mybatis.cache.redis.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * 带Redis缓存功能的Mapper复合标记
 * @author Storezhang
 */
@Mapper
@CacheNamespace(implementation = RedisCache.class)
public @interface RedisMapper {
}
