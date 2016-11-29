package com.ruijc.mybatis.cache.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.annotation.PostConstruct;

@ConditionalOnClass(MapperScannerConfigurer.class)
@ConfigurationProperties("mybatis.cache.redis")
public class RedisProperties {

    private long expire;

    public RedisProperties() {
        expire = 5 * 60;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
