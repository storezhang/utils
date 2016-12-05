package com.ruijc.sample.spring.boot.mapper;

import com.ruijc.mybatis.BaseMapper;
import com.ruijc.mybatis.cache.redis.RedisCache;
import com.ruijc.sample.spring.boot.bean.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@CacheNamespace(implementation = RedisCache.class)
public interface IUserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getByUsername(String username);
}
