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

/**
 * DES加密可逆算法
 *
 * @author Storezhang
 */
public class DESUtils {

    public static final String KEY = "12345678";
    public static final String CHAR_SET = "UTF-8";

    private static final byte[] iv;

    static {
        iv = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
    }

    public static String encrypt(String encryptString, String encryptKey) throws Exception {
        encryptKey = StringUtils.substring(encryptKey, 8, '0');

        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes(CHAR_SET));

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String decryptString, String decryptKey) throws Exception {
        decryptKey = StringUtils.substring(decryptKey, 8, '0');

        byte[] byteMi = Base64.getDecoder().decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData, CHAR_SET);
    }

    public static void main(String[] args) throws Exception {
        String keyStr = "123f";
        String plainText = "我是中国人！";

        String encText = encrypt(plainText, keyStr);
        String decString = decrypt(encText, keyStr);

        System.out.println(encText);
        System.out.println(decString);
    }
}
