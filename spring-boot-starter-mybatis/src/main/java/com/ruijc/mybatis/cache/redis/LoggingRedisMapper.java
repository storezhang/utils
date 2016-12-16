package com.ruijc.mybatis.cache.redis;

import org.apache.ibatis.cache.decorators.LruCache;

import java.lang.annotation.*;

/**
 * 带Redis缓存和日志功能的Mapper复合标记
 *
 * @author Storezhang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
@Inherited
public @interface LoggingRedisMapper {

    Class<? extends org.apache.ibatis.cache.Cache> implementation() default LoggingRedisCache.class;

    Class<? extends org.apache.ibatis.cache.Cache> eviction() default LruCache.class;

    long flushInterval() default 0;

    int size() default 1024;

    boolean readWrite() default true;

    boolean blocking() default false;
}
