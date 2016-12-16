package com.ruijc.mybatis.mapper;

import com.ruijc.mybatis.BaseMapper;
import com.ruijc.mybatis.bean.User;
import com.ruijc.mybatis.cache.redis.RedisMapper;

@RedisMapper
public interface IUserMapper extends BaseMapper<User> {

}
