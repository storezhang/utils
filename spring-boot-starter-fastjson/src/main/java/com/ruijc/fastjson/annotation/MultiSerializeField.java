package com.ruijc.fastjson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多序列化
 *
 * @author Storezhang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiSerializeField {

    Class clazz();

    String[] includes() default {};

    String[] excludes() default {};
}
