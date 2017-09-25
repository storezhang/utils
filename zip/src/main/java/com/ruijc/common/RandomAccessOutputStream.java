package com.ruijc.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class RandomAccessOutputStream extends OutputStream {

    static public final int STREAM_SEEK_SET = 0;
    static public final int STREAM_SEEK_CUR = 1;
    private final int initialBufferCapacity = 65536;
    private final java.nio.ByteBuffer buf = ByteBuffer.allocate(initialBufferCapacity);
    private int bufferSize = 0;
    private RandomAccessFile stream;
    private long size = 0;

    public RandomAccessOutputStream(File file, String mode) throws java.io.IOException {
        stream = new java.io.RandomAccessFile(file, mode);
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (bufferSize > 0) {
            stream.write(buf.array(), 0, bufferSize);
            buf.clear();
            bufferSize = 0;
        }
        stream.write(b);
        size += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (bufferSize > 0) {
            stream.write(buf.array(), 0, bufferSize);
            buf.clear();
            bufferSize = 0;
        }
        stream.write(b, off, len);
        size += len;
    }

    @Override
    public void write(int b) throws IOException {
        if (bufferSize < initialBufferCapacity) {
            buf.put((byte) b);
            bufferSize++;
        } else {
            stream.write(buf.array());
            buf.clear();
            bufferSize = 0;
            buf.put((byte) b);
            bufferSize++;
        }

        size++;
    }

    public long seek(long offset, int seekOrigin) throws java.io.IOException {
        if (bufferSize > 0) {
            stream.write(buf.array(), 0, bufferSize);
            buf.clear();
            bufferSize = 0;
        }
        if (seekOrigin == STREAM_SEEK_SET) {
            stream.seek(offset);
        } else if (seekOrigin == STREAM_SEEK_CUR) {
            stream.seek(offset + stream.getFilePointer());
        }
        size = 0;
        return stream.getFilePointer();
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public void close() throws IOException {
        if (bufferSize > 0) {
            stream.write(buf.array());
            buf.clear();
            bufferSize = 0;
        }
        stream.close();
    }
}
