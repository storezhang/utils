package com.ruijc.sevenzip.common;

import com.ruijc.sevenzip.CRC;

import java.io.*;

public class InStreamWithCRC extends InputStream {

    private RandomAccessFile stream;
    private static final int STREAM_SEEK_SET = 0;
    private static final int STREAM_SEEK_CUR = 1;
    private long size;
    private final CRC crc = new CRC();

    public InStreamWithCRC(String fileName) throws FileNotFoundException {
        stream = new RandomAccessFile(new File(fileName), "r");
    }

    public long seek(int offset, int seekOrigin) throws IOException {
        size = 0;
        crc.init();
        if (seekOrigin == STREAM_SEEK_SET) {
            stream.seek(offset);
        } else if (seekOrigin == STREAM_SEEK_CUR) {
            stream.seek(offset + stream.getFilePointer());
        }
        return stream.getFilePointer();
    }


    @Override
    public int read() throws IOException {
        int ret = stream.read();
        crc.updateByte((byte) ret);
        size++;
        return ret;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int ret = stream.read(b);
        crc.update(b, ret);
        size += ret;
        return ret;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int ret = stream.read(b, off, len);
        crc.update(b, off, ret);
        size += ret;
        return ret;
    }

    public void init() {
        size = 0;
        crc.init();
    }

    public long getSize() {
        return size;
    }

    public void releaseStream() throws IOException {
        stream.close();
    }

    public int getCrc() {
        return crc.getDigest();
    }
}
