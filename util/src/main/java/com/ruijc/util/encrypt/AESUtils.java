/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util.encrypt;

import com.ruijc.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {

    public static final String CHAR_SET = "UTF-8";

    public static String encrypt(String content, String key) {
        String ret;

        key = StringUtils.substring(key, 16, '0');

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); //算法/模式/补码方式
            IvParameterSpec ivps = new IvParameterSpec(key.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);

            ret = Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes(CHAR_SET)));
        } catch (Exception e) {
            ret = "";
        }

        return ret;
    }

    public static String decrypt(String content, String key) {
        String ret;

        key = StringUtils.substring(key, 16, '0');

        try {
            byte[] contentBytes = Base64.getDecoder().decode(content);

            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); //"算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(key.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);

            ret = new String(cipher.doFinal(contentBytes), CHAR_SET);
        } catch (Exception e) {
            ret = "";
        }

        return ret;
    }

    public static void main(String[] args) throws Exception {
        String keyStr = "!wN9gZ1NT6KL*%$P";
        String plainText = "我是中国人&it_b_pay=2016-12-21 23:28:49";

        String encText = encrypt(plainText, keyStr);
        String decString = decrypt(encText, keyStr);

        System.out.println(encText);
        System.out.println(decString);
    }
}
