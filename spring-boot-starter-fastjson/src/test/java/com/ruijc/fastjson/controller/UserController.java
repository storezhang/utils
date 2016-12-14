package com.ruijc.fastjson.controller;

import com.ruijc.fastjson.annotation.JSONP;
import com.ruijc.fastjson.annotation.SerializeField;
import com.ruijc.fastjson.bean.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private User data;

    public UserController() {
        data = new User();
        data.setId(1L);
    }

    @GetMapping("/jsonp")
    @JSONP("user")
    public User testJSONP() {
        return data;
    }

    @GetMapping("/rest")
    public User testRest() {
        return data;
    }
}
