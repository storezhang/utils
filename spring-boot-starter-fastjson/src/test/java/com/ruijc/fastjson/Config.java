package com.ruijc.fastjson;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public FastJsonHttpMessageConverter converter() {
        return new TestFastjsonHttpMessageConverter();
    }
}
