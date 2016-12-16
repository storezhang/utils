package com.ruijc.mybatis.mapper;

import com.ruijc.mybatis.BaseMapper;
import com.ruijc.mybatis.bean.User;
import com.ruijc.mybatis.cache.redis.LoggingRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation = LoggingRedisCache.class)
public interface ILoggingUserMapper extends BaseMapper<User> {

}
