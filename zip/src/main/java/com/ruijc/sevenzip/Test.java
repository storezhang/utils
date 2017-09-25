package com.ruijc.sevenzip;

import java.io.File;
import java.io.IOException;

class Test {
    public static void main(String[] args) {
        try {
            SevenZip sevenZip = new SevenZip("/Users/storezhang/Downloads/test.zip", new File("/Users/storezhang/Downlaods/java-7z-archiver-master"));
            sevenZip.createArchive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

