package com.ruijc.shiro;

import com.ruijc.shiro.annotation.SessionUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Shiro Web环境配置
 *
 * @author Storezhang
 */
@Configuration
public class ShiroWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SessionUserArgumentResolver());
    }
}
