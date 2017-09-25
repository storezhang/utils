package com.ruijc.sevenzip.archive;

import com.ruijc.sevenzip.UpdateItem;
import com.ruijc.sevenzip.common.InStreamWithCRC;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SevenZipFolderInStream extends InputStream {
    private InStreamWithCRC stream;

    private boolean fileIsOpen;
    private long filePos;
    private List<UpdateItem> updateItems;
    private int numFiles;
    private int fileIndex;
    private int off;

    private final List<Integer> crcs = new ArrayList<Integer>();
    private final List<Long> sizes = new ArrayList<Long>();


    public void init(List<UpdateItem> updateItems, int off, int numFiles) {
        this.updateItems = updateItems;
        this.numFiles = numFiles;
        fileIndex = 0;
        this.off = off;
        fileIsOpen = false;
    }

    private void openStream() throws FileNotFoundException {
        filePos = 0;
        while (fileIndex < numFiles) {
            stream = new InStreamWithCRC(updateItems.get(off + fileIndex).getFullName());
            fileIndex++;
            stream.init();
            if (stream != null) {
                fileIsOpen = true;
                return;
            }
            sizes.add(0L);
            addDigest();
        }
    }

    private void addDigest() {
        crcs.add(stream.getCrc());
    }

    private void closeStream() throws IOException {
        stream.releaseStream();
        fileIsOpen = false;
        sizes.add(filePos);
        addDigest();
    }


    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }


    public int getCrc(int i) {
        return crcs.get(i);
    }

    @Override
    public int read(byte[] data, int off, int len) throws IOException {
        int processedSize = -1;
        while (len > 0) {
            if (fileIsOpen) {

                int processed2 = stream.read(data, off, len);
                if (!(processed2 > 0)) {
                    closeStream();
                    continue;
                }
                processedSize = processed2;
                filePos += processed2;
                break;
            }
            if (fileIndex >= numFiles)
                break;
            openStream();
        }
        return processedSize;
    }

    public long getFullSize() {
        long size = 0;
        for (Long i : sizes) {
            size += i;
        }

        return size;
    }

    @Override
    public int read() throws IOException {
        byte[] ret = new byte[1];
        if (read(ret) < 0) return -1;

        return ret[0];
    }
}
