package com.ruijc.sevenzip;


public class UpdateItem {
    private long mTime;

    private long size;
    private String name;

    private String fullName;

    private int attrib;

    private boolean newData;

    private boolean isAnti = false;
    private boolean isDir = false;

    private boolean attribDefined = false;
    private boolean mTimeDefined = false;

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttrib() {
        return attrib;
    }

    public void setAttrib(int attrib) {
        this.attrib = attrib;
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public void setIsAnti(boolean isAnti) {
        this.isAnti = isAnti;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setIsDir(boolean isDir) {
        this.isDir = isDir;
    }

    public boolean isAttribDefined() {
        return attribDefined;
    }

    public void setAttribDefined(boolean attribDefined) {
        this.attribDefined = attribDefined;
    }

    public boolean ismTimeDefined() {
        return mTimeDefined;
    }

    public void setmTimeDefined(boolean mTimeDefined) {
        this.mTimeDefined = mTimeDefined;
    }

    public boolean hasStream() {
        return !isDir && !isAnti && size != 0;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
