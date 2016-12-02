package com.ruijc.shiro.controller;

import com.ruijc.fastjson.annotation.JSONP;
import com.ruijc.shiro.EncryptToken;
import com.ruijc.shiro.bean.User;
import com.ruijc.shiro.mapper.IUserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserMapper userMapper;

    @GetMapping("/test")
    @JSONP("user")
    public User test() {
        return userMapper.getByUsername("storezhang@gmail.com");
    }

    @PostMapping("/login")
    public User login(String username, String password) {
        User user;

        Subject subject = SecurityUtils.getSubject();
        EncryptToken token = new EncryptToken(username, password, true);
        token.setEncrypted(true);
        try {
            subject.login(token);
        } catch (Exception e) {
            user = null;
            return user;
        }

        user = userMapper.getByUsername(username);

        return user;
    }
}
