/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import java.util.Random;

/**
 * 密码工具类
 *
 * @author Storezhang
 */
public class PasswordUtils {

    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', '!', '@', '#', '$', '%', '^', '&', '*', '~', '|'};//72个字符集

    /**
     * @param len 随机密码长度
     * @return 随机密码数
     */
    public static String genPasswd(int len) {
        Random random = new Random();
        StringBuilder password = new StringBuilder("");//保存生成密码的变量
        for (int m = 1; m <= len; ++m) {//内循环从1开始到密码长度正式开始生成密码
            char ch = CHARS[random.nextInt(72)];
            if (!testRepeat(password.toString(), ch)) {
                password.append(ch);//为密码变量随机增加上面字符中的一个
            } else {
                m--;
            }
        }

        return password.toString();//将生成出来的密码赋值给密码数组
    }

    public static String genPasswd() {
        return genPasswd(10);
    }

    private static boolean testRepeat(String passwd, char passwdCh) {
        if (StringUtils.isBlank(passwd)) {
            return false;
        }

        return -1 != passwd.indexOf(passwdCh);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100 * 100 * 100; ++i) {
            System.out.println("--->>>" + genPasswd(10));
        }
    }
}
