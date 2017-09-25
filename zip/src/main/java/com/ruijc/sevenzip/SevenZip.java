package com.ruijc.sevenzip;

import com.ruijc.common.RandomAccessOutputStream;
import com.ruijc.sevenzip.archive.ArchiveDatabase;
import com.ruijc.sevenzip.archive.FileItem;
import com.ruijc.sevenzip.archive.OutArchive;
import com.ruijc.sevenzip.archive.SevenZipFolderInStream;
import com.ruijc.sevenzip.compression.lzma.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SevenZip {
    private final Logger logger = LoggerFactory.getLogger(SevenZip.class);

    private final List<UpdateItem> updateItems = new ArrayList<UpdateItem>();
    private final List<UpdateItem> emptyRefs = new ArrayList<UpdateItem>();
    private RandomAccessOutputStream outStream;

    private void collectFiles(String path, File... files) {
        for (File file : files) {
            if (!file.canRead()) {
                logger.warn("Can't read from file %:", file.getAbsolutePath());
                continue;
            }
            UpdateItem ui = new UpdateItem();
            ui.setNewData(true);
            ui.setIsAnti(false);
            ui.setSize(0);
            ui.setmTime(file.lastModified());
            ui.setmTimeDefined(file.lastModified() != 0L);
            ui.setmTimeDefined(true);
            String childName;
            if (!path.isEmpty()) {
                childName = path + File.separator + file.getName();
            } else {
                childName = file.getName();
            }
            ui.setName(childName);
            ui.setFullName(file.getAbsolutePath());
            ui.setIsDir(file.isDirectory());
            ui.setIsAnti(false);
            ui.setSize(file.length());
            if (file.isDirectory()) {
                ui.setAttrib(16);
            }  else {
                ui.setAttrib(32);
            }
            ui.setAttribDefined(true);
            updateItems.add(ui);
            if (file.isDirectory()) {
                collectFiles(childName, file.listFiles());
            }
        }
    }

    public SevenZip(String archiveName, File... files) throws IOException {
        logger.info("Compression started");
        collectFiles("", files);
        outStream = new RandomAccessOutputStream(new File(archiveName), "rw");
    }

    private static void setMethodProperties(com.ruijc.sevenzip.compression.lzma.Encoder encoder, long inSizeForReduce, LZMACoderInfo info) {
        boolean tryReduce = false;
        int dictionarySize = 1 << 24;
        int reducedDictionarySize = 1 << 10;
        if (inSizeForReduce != 0) {
            for (; ; ) {
                int step = (reducedDictionarySize >> 1);
                if (reducedDictionarySize >= inSizeForReduce) {
                    tryReduce = true;
                    break;
                }
                reducedDictionarySize += step;
                if (reducedDictionarySize >= inSizeForReduce) {
                    tryReduce = true;
                    break;
                }
                if (reducedDictionarySize >= (3 << 30))
                    break;
                reducedDictionarySize += step;
            }
        }
        if (tryReduce) {
            if (reducedDictionarySize < dictionarySize) {
                dictionarySize = reducedDictionarySize;
            }
        }
        info.setDictionarySize(dictionarySize);
        encoder.setDictionarySize(dictionarySize);
        encoder.setNumFastBytes(32);
        encoder.setMatchFinder(1);
        encoder.setLcLpPb(3, 0, 2);
    }

    public void createArchive() throws IOException {
        OutArchive archive = new OutArchive();
        ArchiveDatabase newDatabase = new ArchiveDatabase();
        long kLzmaDicSizeX5 = 1 << 24;
        long numSolidFiles = Long.MAX_VALUE;
        long numSolidBytes = kLzmaDicSizeX5 << 7;
        long inSizeForReduce = 0;

        for (UpdateItem updateItem : updateItems) {
            if (updateItem.isNewData()) {
                inSizeForReduce += updateItem.getSize();
            }
        }

        if (inSizeForReduce < 0) {
            inSizeForReduce = 0;
        }
        long kMinReduceSize = 1 << 16;
        if (inSizeForReduce < kMinReduceSize) {
            inSizeForReduce = kMinReduceSize;
        }

        archive.create(outStream);
        archive.skipPrefixArchiveHeader();
        Encoder encoder = new Encoder();
        int numSubFiles;
        LZMACoderInfo info = new LZMACoderInfo();
        setMethodProperties(encoder, inSizeForReduce, info);

        /*for (UpdateItem item : updateItems) {
            if (!item.isNewData() || !item.hasStream()) {
                emptyRefs.add(item);
            }
        }
        for (UpdateItem item : emptyRefs) {
            emptyRefs.remove(item);
        }*/
        // emptyRefs.addAll(updateItems.stream().filter(ui -> !ui.isNewData() || !ui.hasStream()).collect(Collectors.toList()));
        // emptyRefs.forEach(updateItems::remove);

        for (int i = 0; i < updateItems.size(); ) {
            long totalSize = 0;
            for (numSubFiles = 0; i + numSubFiles < updateItems.size() && numSubFiles < numSolidFiles; numSubFiles++) {
                totalSize += updateItems.get(i + numSubFiles).getSize();
                if (totalSize > numSolidBytes) {
                    break;
                }
            }
            if (numSubFiles < 1) {
                numSubFiles = 1;
            }
            Folder folder = new Folder();
            folder.getCoders().add(info);
            int numUnpackStreams = 0;

            SevenZipFolderInStream inStream = new SevenZipFolderInStream();
            inStream.init(updateItems, i, numSubFiles);
            encoder.code(inStream, outStream);

            folder.addUnpackSize(inStream.getFullSize());
            for (int j = i; j < i + numSubFiles; j++) {
                FileItem file = new FileItem();
                UpdateItem ui = updateItems.get(j);
                file.setName(ui.getName());

                if (ui.isAttribDefined()) {
                    file.setAttributes(ui.getAttrib());
                    file.setAttributesDefined(true);
                }
                file.setSize(ui.getSize());
                file.setDirectory(ui.isDir());
                file.setHasStream(ui.hasStream());

                if (file.getSize() != 0) {
                    file.setCrcDefined(true);
                    file.setFileCRC(inStream.getCrc(j - i));
                    numUnpackStreams++;
                } else {
                    file.setCrcDefined(false);
                    file.setHasStream(false);
                }
                newDatabase.addMTimeDefined(ui.ismTimeDefined());
                if (ui.ismTimeDefined()) {
                    newDatabase.addMTime(ui.getmTime());
                }
                newDatabase.addFile(file);
            }
            newDatabase.addFolder(folder);
            newDatabase.addPackSize(outStream.getSize());
            outStream.setSize(0);
            newDatabase.getNumUnPackStreamsVector().add(numUnpackStreams);
            i += numSubFiles;
        }

        fillEmptyRefs(newDatabase);
        archive.writeDatabase(newDatabase);
        outStream.close();
    }

    private void fillEmptyRefs(ArchiveDatabase archiveDatabase) {
        for (UpdateItem emptyRef : emptyRefs) {
            FileItem file = new FileItem();
            file.setName(emptyRef.getName());
            if (emptyRef.isAttribDefined()) {
                file.setAttributes(emptyRef.getAttrib());
                file.setAttributesDefined(true);
            }

            file.setSize(emptyRef.getSize());
            file.setDirectory(emptyRef.isDir());
            file.setHasStream(emptyRef.hasStream());
            archiveDatabase.addMTimeDefined(emptyRef.ismTimeDefined());
            if (emptyRef.ismTimeDefined()) {
                archiveDatabase.addMTime(emptyRef.getmTime());
            }
            archiveDatabase.addFile(file);
        }
    }
}
