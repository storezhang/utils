package com.ruijc.shiro.controller;

import com.ruijc.Response;
import com.ruijc.fastjson.annotation.JSONP;
import com.ruijc.fastjson.annotation.SerializeField;
import com.ruijc.shiro.bean.User;
import com.ruijc.shiro.logic.UserL;
import com.ruijc.shiro.mapper.IUserMapper;
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
    private UserL userL;

    @PostMapping("/login")
    @SerializeField(clazz = User.class, excludes = {"id", "key", "password"})
    public Response login(String username, String password) {
        Response res = new Response();

        Subject subject = SecurityUtils.getSubject();
        EncryptToken token = new EncryptToken(username, password, true);
        token.setEncrypted(true);
        try {
            subject.login(token);
        } catch (Exception e) {
            res.setCode(Response.FAILD);
            return res;
        }

        return res;
    }

    @PostMapping("/register")
    public Response register(User user) {
        Response res = new Response();
        if (!userL.add(user)) {
            res.setCode(Response.FAILD);
            return res;
        }

        return res;
    }

    @GetMapping("/logout")
    public Response logout() {
        Response res = new Response();
        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            res.setCode(Response.FAILD);
        }

        return res;
    }
}
