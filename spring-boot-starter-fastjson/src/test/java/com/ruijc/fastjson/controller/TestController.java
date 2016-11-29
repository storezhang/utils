package com.ruijc.fastjson.controller;

import com.ruijc.fastjson.annotation.JSONP;
import com.ruijc.fastjson.annotation.SerializeField;
import com.ruijc.fastjson.bean.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    private User data;

    public TestController() {
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

    @GetMapping("/includes")
    @SerializeField(clazz = Map.class, includes = {"id"})
    public User testIncludes() {
        return data;
    }
}
