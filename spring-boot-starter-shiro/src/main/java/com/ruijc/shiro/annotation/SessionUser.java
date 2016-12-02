package com.ruijc.shiro.annotation;

import java.lang.annotation.*;

/**
 * 获取Shiro当前用户
 *
 * @author Storezhang
 * @see SessionUserArgumentResolver
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionUser {
}
