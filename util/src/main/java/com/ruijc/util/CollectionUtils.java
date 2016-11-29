/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import java.util.Arrays;
import java.util.List;

/**
 * 集合工具类
 *
 * @author Storezhang
 */
public class CollectionUtils {

    /**
     * 获得最后一个元素
     *
     * @param <T>  泛型
     * @param data 数据
     * @return 最后一个元素
     */
    public static <T> T last(List<T> data) {
        return data.get(data.size() - 1);
    }

    /**
     * 组装一个列表的字符串形式
     *
     * @param <T>   类型
     * @param data  列表
     * @param split 分隔符
     * @return 字符串形式
     */
    public static <T> String strings(List<T> data, String split) {
        StringBuilder sb = new StringBuilder();

        if (null == data || data.isEmpty()) {
            sb.append("");
        } else {
            sb.append(split);
            for (T obj : data) {
                sb.append(obj.toString());
                sb.append(split);
            }
        }

        return sb.toString();
    }

    /**
     * 组装一个列表的字符串形式
     *
     * @param <T>  类型
     * @param data 列表
     * @return 字符串形式
     */
    public static <T> String strings(List<T> data) {
        return strings(data, "\n");
    }

    public static <T> T[] concat(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    public static boolean isBlank(byte[] data) {
        return (null == data || 0 == data.length);
    }

    public static <T> boolean isBlank(List<T> data) {
        return null == data || data.isEmpty();
    }
}
