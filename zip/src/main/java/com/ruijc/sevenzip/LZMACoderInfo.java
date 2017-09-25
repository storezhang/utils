package com.ruijc.sevenzip;

import com.ruijc.common.ByteBuffer;


public class LZMACoderInfo extends CoderInfo {
    private long dictionarySize = 1 << 16;

    @Override
    public ByteBuffer getProps() {
        ByteBuffer buf = new ByteBuffer();
        buf.setCapacity(5);
        byte[] properties = buf.data();
        properties[0] = (byte) (93);
        for (int i = 0; i < 4; i++)
            properties[1 + i] = (byte) (dictionarySize >> (8 * i));
        return buf;
    }

    public void setDictionarySize(long size) {
        dictionarySize = size;
    }

    @Override
    public boolean isSimpleCoder() {
        return true;
    }

    @Override
    public long getMethodID() {
        return 0x030101;
    }

    @Override
    public int getNumInStreams() {
        return 1;
    }

    @Override
    public int getNumOutStreams() {
        return 1;
    }
}
