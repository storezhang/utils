package com.ruijc.mybatis;

import com.ruijc.mybatis.mapper.IUserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisTests.class)
@EnableRedisRepositories
@SpringBootApplication
public class MybatisTests {

    @Autowired
    private IUserMapper userMapper;

    @Test
    public void testCache() {
        for (int i = 0; i < 10; ++i) {
            Assert.assertTrue(4 == userMapper.selectAll().size());
        }
    }

}
