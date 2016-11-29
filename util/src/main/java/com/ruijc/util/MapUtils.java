/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 映射工具类
 *
 * @author Storezhang
 */
public class MapUtils {

    public static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = null;
        if (obj == null) {
            return map;
        }

        map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            map = null;
        }

        return map;
    }

    public static Map<String, String> toStringMap(Object obj) {
        Map<String, String> map = null;
        if (obj == null) {
            return map;
        }

        map = new HashMap<String, String>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    map.put(key, getter.invoke(obj).toString());
                }

            }
        } catch (Exception e) {
            map = null;
        }

        return map;
    }

    public static int getInt(Map<Object, Object> dataMap, Object key) {
        int ret;
        Object data = dataMap.get(key);
        try {
            ret = Integer.parseInt(data.toString());
        } catch (NumberFormatException e) {
            ret = 0;
        }
        return ret;
    }

    public static double getDouble(Map<Object, Object> dataMap, Object key) {
        double ret;
        Object data = dataMap.get(key);
        try {
            ret = Double.parseDouble(data.toString());
        } catch (NumberFormatException e) {
            ret = 0;
        }
        return ret;
    }

    public static long getLong(Map<Object, Object> dataMap, Object key) {
        long ret;
        Object data = dataMap.get(key);
        try {
            ret = Long.parseLong(data.toString());
        } catch (NumberFormatException e) {
            ret = 0;
        }
        return ret;
    }

    public static String getString(Map<Object, Object> dataMap, Object key) {
        Object data = dataMap.get(key);
        return data.toString();
    }

    public static boolean check(Map<String, Boolean> data, String key) {
        boolean success;
        if (!data.containsKey(key)) {
            success = false;
        } else {
            success = data.get(key);
        }

        return success;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用逻辑与字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String linkString(Map<String, String> params) {
        String ret = "";

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                ret = ret + key + "=" + value;
            } else {
                ret = ret + key + "=" + value + "&";
            }
        }

        return ret;
    }
}
