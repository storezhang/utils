package com.ruijc.mybatis.mapper;

import com.ruijc.mybatis.BaseMapper;
import com.ruijc.mybatis.bean.User;
import com.ruijc.mybatis.cache.redis.LoggingRedisMapper;

@LoggingRedisMapper
public interface ILoggingUserMapper extends BaseMapper<User> {

}
