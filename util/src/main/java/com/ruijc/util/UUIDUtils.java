/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

/**
 * UUID工具类
 *
 * @author storezhang
 */

import com.ruijc.util.encrypt.Base58Utils;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class UUIDUtils {

    /**
     * 生成普通的UUID
     *
     * @return 生成普通的UUID
     */
    public static String uuid() {
        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }

    /**
     * 生成Base64压缩后的UUID
     *
     * @return 压缩后的UUID
     */
    public static String base64Uuid() {
        UUID uuid = UUID.randomUUID();
        return base64Uuid(uuid);
    }

    /**
     * 生成Base58压缩后的UUID
     *
     * @return Base58压缩后的UUID
     */
    public static String base58Uuid() {
        UUID uuid = UUID.randomUUID();

        return base58Uuid(uuid);
    }

    private static String base58Uuid(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return Base58Utils.encode(bb.array());
    }

    private static String base64Uuid(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return Base64.getUrlEncoder().encodeToString(bb.array());
    }

    public static String compress(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);

        return base64Uuid(uuid);
    }

    public static String uncompress(String compressedUuid) {
        if (compressedUuid.length() != 22) {
            throw new IllegalArgumentException("Invalid uuid!");
        }
        byte[] byUuid = Base64.getUrlDecoder().decode(compressedUuid + "==");
        long most = bytesToLong(byUuid, 0);
        long least = bytesToLong(byUuid, 8);
        UUID uuid = new UUID(most, least);

        return uuid.toString();
    }

    private static long bytesToLong(byte[] bytes, int offset) {
        long value = 0;
        for (int i = 7; i > -1; i--) {
            value |= (((long) bytes[offset++]) & 0xFF) << 8 * i;
        }

        return value;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; ++i) {
            System.err.println("--->" + base58Uuid());
        }
    }
}
