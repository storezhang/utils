// SevenZip/CRC.java

package com.ruijc.sevenzip;

public class CRC {
    private static final int[] table = new int[256];

    static {
        for (int i = 0; i < 256; i++) {
            int r = i;
            for (int j = 0; j < 8; j++)
                if ((r & 1) != 0)
                    r = (r >>> 1) ^ 0xEDB88320;
                else
                    r >>>= 1;
            table[i] = r;
        }
    }

    private int _value = -1;

    public void init() {
        _value = -1;
    }

    public void updateUInt32(int v) {
        for (int i = 0; i < 4; i++)
            updateByte((byte) ((v >> (8 * i)) & 0xFF));
    }

    public void updateUInt64(long v) {
        for (int i = 0; i < 8; i++)
            updateByte((byte) ((byte) ((v >> (8 * i))) & 0xFF));
    }

    public void update(byte[] data, int offset, int size) {
        for (int i = 0; i < size; i++)
            _value = table[(_value ^ data[offset + i]) & 0xFF] ^ (_value >>> 8);
    }

    public void Update(byte[] data) {
        int size = data.length;
        for (byte aData : data) {
            _value = table[(_value ^ aData) & 0xFF] ^ (_value >>> 8);
        }
    }

    public void updateByte(byte b) {
        _value = table[(_value ^ b) & 0xFF] ^ (_value >>> 8);
    }

    public void updateByte(int b) {
        _value = table[(_value ^ b) & 0xFF] ^ (_value >>> 8);
    }

    public void update(byte[] data, int size) {
        for (int i = 0; i < size; i++)
            _value = table[(_value ^ data[i]) & 0xFF] ^ (_value >>> 8);
    }

    public int getDigest() {
        return ~_value;
    }

    public static int calculateDigest(byte[] data, int size) {
        CRC crc = new CRC();
        crc.update(data, size);
        return crc.getDigest();
    }

    public static int calculateDigest(byte[] data, int offset, int size) {
        CRC crc = new CRC();
        crc.update(data, offset, size);
        return crc.getDigest();
    }

    static public boolean verifyDigest(int digest, byte[] data, int size) {
        return (calculateDigest(data, size) == digest);
    }
}
