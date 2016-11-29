/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import com.ruijc.util.encrypt.AESUtils;
import com.ruijc.util.encrypt.DESUtils;
import com.ruijc.util.encrypt.HexUtils;
import com.ruijc.util.encrypt.MD5Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 常用加密算法
 *
 * @author Storezhang
 */
public class EncryptUtils {

    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     * @param key  加密使用的key
     * @return 生成MD5编码的字符串
     * @throws InvalidKeyException      异常
     * @throws NoSuchAlgorithmException 异常
     */
    public static String hmacSHA1(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);

        return md5(rawHmac);
    }

    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     * @param key  加密使用的key
     * @return 生成MD5编码的字符串
     * @throws InvalidKeyException      异常
     * @throws NoSuchAlgorithmException 异常
     */
    public static String hmacSHA1(String data, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        return hmacSHA1(data.getBytes(), key.getBytes());
    }

    /**
     * MD5加密算法
     *
     * @param data 要加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String data) {
        return MD5Utils.md5Hex(data);
    }

    /**
     * MD5加密算法
     *
     * @param data 要加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(byte[] data) {
        return MD5Utils.md5Hex(data);
    }

    /**
     * DES加密
     *
     * @param key     加密密钥
     * @param encrypt 要加密的字符串
     * @return 加密后的字符串
     */
    public static String desEncrypt(String key, String encrypt) {
        String out;
        try {
            out = DESUtils.encrypt(encrypt, key);
        } catch (Exception e) {
            out = "";
        }

        return out;
    }

    /**
     * DES解密
     *
     * @param key     解密密钥
     * @param decrypt 要解密的字符串
     * @return 解密后的字符串
     */
    public static String desDecrypt(String key, String decrypt) {
        String out;
        try {
            out = DESUtils.decrypt(decrypt, key);
        } catch (Exception e) {
            out = "";
        }

        return out;
    }

    /**
     * AES加密
     *
     * @param key     加密密钥
     * @param encrypt 要加密的字符串
     * @return 加密后的字符串
     */
    public static String aesEncrypt(String key, String encrypt) {
        return AESUtils.encrypt(encrypt, key);
    }

    /**
     * AES解密
     *
     * @param key     解密密钥
     * @param decrypt 要解密的字符串
     * @return 解密后的字符串
     */
    public static String aesDecrypt(String key, String decrypt) {
        return AESUtils.decrypt(decrypt, key);
    }

    public static String base64Encrypt(String from) {
        return new String(Base64.getEncoder().encode(from.getBytes()));
    }

    public static String base64Decrypt(String from) {
        return new String(Base64.getDecoder().decode(from));
    }

    private static String sha1(String data) {
        String ret;

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            ret = "";
            return ret;
        }

        return HexUtils.toHexString(digest.digest(data.getBytes()));
    }

}
