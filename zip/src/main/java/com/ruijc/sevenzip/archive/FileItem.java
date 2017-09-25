package com.ruijc.sevenzip.archive;

public class FileItem {
    private int attributes;
    private int fileCRC;

    private boolean directory;
    private boolean attributesDefined;
    private boolean hasStream;
    private long size;

    private boolean crcDefined;

    public boolean isCrcDefined() {
        return crcDefined;
    }

    public void setCrcDefined(boolean crcDefined) {
        this.crcDefined = crcDefined;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }

    public int getFileCRC() {
        return fileCRC;
    }

    public void setFileCRC(int fileCRC) {
        this.fileCRC = fileCRC;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public boolean isAttributesDefined() {
        return attributesDefined;
    }

    public void setAttributesDefined(boolean areAttributesDefined) {
        this.attributesDefined = areAttributesDefined;
    }

    public boolean hasStream() {
        return hasStream;
    }

    public void setHasStream(boolean hasStream) {
        this.hasStream = hasStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public FileItem() {
        hasStream = true;
        directory = false;
        attributesDefined = false;
    }
}
