package com.ruijc.mybatis.cache;

import com.ruijc.util.serialize.ISerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Redis工具类
 *
 * @author Storezhang
 */
@Component
public class SerializerUtils {

    private static ISerializer<Object> proxy;

    @Autowired
    private ISerializer<Object> self;

    public static ISerializer<Object> getSerializer() {
        return proxy;
    }

    @PostConstruct
    public void init() {
        proxy = self;
    }
}
