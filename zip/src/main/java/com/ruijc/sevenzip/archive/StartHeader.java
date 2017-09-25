package com.ruijc.sevenzip.archive;

public class StartHeader {
    private long nextHeaderOffset;
    private long nextHeaderSize;
    private int nextHeaderCRC;

    public long getNextHeaderOffset() {
        return nextHeaderOffset;
    }

    public void setNextHeaderOffset(long nextHeaderOffset) {
        this.nextHeaderOffset = nextHeaderOffset;
    }

    public long getNextHeaderSize() {
        return nextHeaderSize;
    }

    public void setNextHeaderSize(long nextHeaderSize) {
        this.nextHeaderSize = nextHeaderSize;
    }

    public int getNextHeaderCRC() {
        return nextHeaderCRC;
    }

    public void setNextHeaderCRC(int nextHeaderCRC) {
        this.nextHeaderCRC = nextHeaderCRC;
    }
}
