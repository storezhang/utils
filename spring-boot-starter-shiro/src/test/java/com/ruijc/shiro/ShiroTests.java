package com.ruijc.shiro;

import com.ruijc.shiro.mapper.IUserMapper;
import org.apache.shiro.authc.credential.PasswordService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShiroTests.class)
@EnableRedisRepositories
@SpringBootApplication
@EnableWebMvc
public class ShiroTests {

    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private PasswordService passwordSvc;

    @Test
    public void testCache() {
        for (int i = 0; i < 10; ++i) {
            Assert.assertTrue(1 == userMapper.selectAll().size());
        }
    }

    @Test
    public void testPasswordService() {
        System.err.println("---->" + passwordSvc.encryptPassword("12345678"));
    }
}
