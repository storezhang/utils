/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字工具类
 *
 * @author Storezhang
 */
public class NumberUtils {

    /**
     * 从字符串中取得整数
     *
     * @param data 字符串
     * @return 整数
     */
    public static int getInt(String data, int defaultValue) {
        int ret;
        try {
            ret = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            ret = defaultValue;
        }

        return ret;
    }

    public static int getInt(String data) {
        return getInt(data, 0);
    }

    /**
     * 从字符串中取得实数
     *
     * @param data 字符串
     * @return 实数
     */
    public static double getDouble(String data) {
        double ret;
        try {
            ret = Double.parseDouble(data);
        } catch (NumberFormatException e) {
            ret = 0;
        }
        return ret;
    }

    /**
     * 从字符串读取出整形数据
     *
     * @param data 原始字符串
     * @return 整形列表
     */
    public static List<Integer> getInts(String data) {
        return getInts(data, ",");
    }

    /**
     * 从字符串读取出整形数据
     *
     * @param data  原始字符串
     * @param space 分隔符
     * @return 整形列表
     */
    public static List<Integer> getInts(String data, String space) {
        List<Integer> ints = new ArrayList<Integer>();

        String[] intArray = StringUtils.list(data, space);
        if (null == intArray || intArray.length == 0) {
            return ints;
        }

        for (String intString : intArray) {
            try {
                ints.add(Integer.parseInt(intString));
            } catch (NumberFormatException e) {
                continue;
            }
        }

        return ints;
    }
}
