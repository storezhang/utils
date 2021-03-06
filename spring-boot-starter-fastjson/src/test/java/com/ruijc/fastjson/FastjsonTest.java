package com.ruijc.fastjson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MockServletContext.class)
@SpringBootApplication
public class FastjsonTest {

    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testJsonp() throws Exception {
        mvc.perform(get("/test/jsonp"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("user({\"id\":1,\"key\":\"User-1\"})")));
    }

    @Test
    public void testRest() throws Exception {
        mvc.perform(get("/test/rest"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"key\":\"User-1\"}")));
    }

    @Test
    public void testSerializeField() throws Exception {
        mvc.perform(get("/test/field/serialize"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1}")));
    }

    @Test
    public void testMoreSerializeField() throws Exception {
        mvc.perform(get("/test/field/serialize/more"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"blog\":{\"id\":1},\"user\":{}}")));
    }
}
