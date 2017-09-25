package com.ruijc.sevenzip;

import java.util.ArrayList;
import java.util.List;


public class Folder {
    private final List<Long> unpackSizes = new ArrayList<Long>();
    private final List<CoderInfo> coders = new ArrayList<CoderInfo>();

    public void addUnpackSize(long size) {
        unpackSizes.add(size);
    }

    public List<Long> getUnpackSizes() {
        return unpackSizes;
    }

    public List<CoderInfo> getCoders() {
        return coders;
    }
}
