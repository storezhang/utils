package com.ruijc.sample.spring.boot.logic;

import com.ruijc.sample.spring.boot.bean.User;
import com.ruijc.sample.spring.boot.mapper.IUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserL {

    @Autowired
    private IUserMapper userMapper;

    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }
}
