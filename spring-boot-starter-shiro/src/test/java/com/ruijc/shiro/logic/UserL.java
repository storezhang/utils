package com.ruijc.shiro.logic;

import com.ruijc.shiro.mapper.IUserMapper;
import com.ruijc.shiro.bean.User;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserL {

    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private PasswordService pawordSvc;

    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    public boolean add(User user) {
        user.setPassword(pawordSvc.encryptPassword(user.getPassword()));
        return userMapper.insert(user) > 0;
    }
}
