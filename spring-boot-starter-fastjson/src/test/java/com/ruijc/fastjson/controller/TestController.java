package com.ruijc.fastjson.controller;

import com.ruijc.fastjson.annotation.JSONP;
import com.ruijc.fastjson.annotation.MoreSerializeField;
import com.ruijc.fastjson.annotation.SerializeField;
import com.ruijc.fastjson.bean.Blog;
import com.ruijc.fastjson.bean.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    private User user;
    private Blog blog;

    public TestController() {
        user = new User();
        user.setId(1L);

        blog = new Blog();
        blog.setId(1L);
    }

    @GetMapping("/jsonp")
    @JSONP("user")
    public User testJSONP() {
        return user;
    }

    @GetMapping("/rest")
    public User testRest() {
        return user;
    }

    @GetMapping("/field/serialize")
    @SerializeField(clazz = User.class, excludes = {"key"})
    public User testSerializeField() {
        return user;
    }

    @GetMapping("/field/serialize/more")
    @MoreSerializeField({
            @SerializeField(clazz = User.class, excludes = {"id", "key"}),
            @SerializeField(clazz = Blog.class, excludes = {"key"})
    })
    public Map<String, Object> testMoreSerializeField() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", user);
        data.put("blog", blog);

        return data;
    }
}
