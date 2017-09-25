package com.ruijc.sevenzip.archive;

import com.ruijc.sevenzip.Folder;

import java.util.ArrayList;
import java.util.List;

public final class ArchiveDatabase {
    private final List<Long> packSizes = new ArrayList<Long>();
    private final List<Folder> folders = new ArrayList<Folder>();
    private final List<Integer> numUnPackStreamsVector = new ArrayList<Integer>();
    private final List<FileItem> files = new ArrayList<FileItem>();
    private final List<Long> mTimes = new ArrayList<Long>();
    private final List<Boolean> mTimesDefined = new ArrayList<Boolean>();


    public List<Long> getMTimes() {
        return mTimes;
    }

    public List<Boolean> getMTimesDefined() {
        return mTimesDefined;
    }

    public void addMTime(long mTime) {
        mTimes.add(mTime);
    }

    public void addMTimeDefined(boolean mTimeDefined) {
        mTimesDefined.add(mTimeDefined);
    }


    boolean isEmpty() {
        return (packSizes.isEmpty() &&
                folders.isEmpty() &&
                numUnPackStreamsVector.isEmpty() &&
                files.isEmpty());
    }

    public void addFolder(Folder f) {
        folders.add(f);
    }

    public List<Long> getPackSizes() {
        return packSizes;
    }

    public void addPackSize(long packSize) {
        packSizes.add(packSize);
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public List<Integer> getNumUnPackStreamsVector() {
        return numUnPackStreamsVector;
    }

    public List<FileItem> getFiles() {
        return files;
    }

    public void addFile(FileItem file) {
        files.add(file);
    }
}
