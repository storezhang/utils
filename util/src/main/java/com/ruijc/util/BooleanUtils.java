/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

/**
 * 布尔工具类
 *
 * @author Storezhang
 */
public class BooleanUtils {

    /**
     * 解析出布尔值的整形数
     *
     * @param bool 布尔值
     * @return 整形数
     */
    public static int parse(Boolean bool) {
        if (null == bool || bool) {
            return 1;
        }
        return 0;
    }
}
