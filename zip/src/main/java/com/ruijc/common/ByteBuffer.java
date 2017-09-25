package com.ruijc.common;


public class ByteBuffer {
    private int capacity;
    private byte[] items;

    public ByteBuffer() {
        capacity = 0;
        items = null;
    }

    public byte[] data() {
        return items;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int newCapacity) {
        if (newCapacity == capacity) {
            return;
        }
        byte[] newBuffer;
        if (newCapacity > 0) {
            newBuffer = new byte[newCapacity];
            if (capacity > 0) {
                int len = capacity;
                if (newCapacity < len) len = newCapacity;
                System.arraycopy(items, 0, newBuffer, 0, len);
            }
        } else
            newBuffer = null;

        items = newBuffer;
        capacity = newCapacity;
    }
}
