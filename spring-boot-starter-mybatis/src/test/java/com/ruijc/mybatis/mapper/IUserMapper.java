package com.ruijc.mybatis.mapper;

import com.ruijc.mybatis.BaseMapper;
import com.ruijc.mybatis.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IUserMapper extends BaseMapper<User> {

    int countAll();
}
