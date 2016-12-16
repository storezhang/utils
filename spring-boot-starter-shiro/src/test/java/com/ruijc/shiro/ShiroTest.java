package com.ruijc.shiro;

import org.apache.shiro.util.ThreadContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MockServletContext.class)
@SpringBootApplication
@EnableRedisRepositories
public class ShiroTest {

    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        org.apache.shiro.mgt.SecurityManager securityManger = mock(org.apache.shiro.mgt.SecurityManager.class, RETURNS_DEEP_STUBS);
        ThreadContext.bind(securityManger);
    }

    @Test
    public void testLogin() throws Exception {
        mvc.perform(
                post("/user/login")
                        .param("username", "storezhang@gmail.com")
                        .param("password", "12345678")
        ).andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"code\":200}")));

        mvc.perform(get("/user/logout")).andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"code\":200}")));
    }

    @Test
    public void testRegister() throws Exception {
        mvc.perform(
                post("/user/register")
                        .param("username", "10290688@gmail.com")
                        .param("password", "12345678")
        ).andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"code\":200}")));

        mvc.perform(
                post("/user/login")
                        .param("username", "10290688@gmail.com")
                        .param("password", "12345678")
        ).andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"code\":200}")));

        mvc.perform(get("/user/logout")).andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"code\":200}")));
    }
}
