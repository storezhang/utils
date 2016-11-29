/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 随机工具类
 *
 * @author Storezhang
 */
public class RandomUtils {

    public static <T> Set<T> randomList(List<T> data, int num) {
        Set<T> ret = new HashSet<T>();
        for (int i = 0; i < num; ++i) {
            T item = random(data);
            ret.add(item);
        }
        return ret;
    }

    public static <T> T random(List<T> data) {
        Random random = new Random(System.currentTimeMillis());
        int len = data.size();
        int index = random.nextInt(len);
        return data.get(index);
    }

    public static int nextInt(int max) {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(max);
    }

    public static int nextInt(int min, int max) {
        Random random = new Random(System.currentTimeMillis());
        int ret = random.nextInt(max);
        if (ret >= min) {
            return ret;
        } else {
            return ret + min;
        }
    }
}
