package com.ruijc.sample.spring.boot.controller;

import com.ruijc.fastjson.annotation.JSONP;
import com.ruijc.sample.spring.boot.bean.User;
import com.ruijc.sample.spring.boot.mapper.IUserMapper;
import com.ruijc.shiro.EncryptToken;
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

    @PostMapping("/login")
    @JSONP("user")
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
