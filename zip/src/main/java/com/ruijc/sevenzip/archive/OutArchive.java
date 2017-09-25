package com.ruijc.sevenzip.archive;

import com.ruijc.common.ByteBuffer;
import com.ruijc.common.OutBuffer;
import com.ruijc.common.RandomAccessOutputStream;
import com.ruijc.sevenzip.CRC;
import com.ruijc.sevenzip.CoderInfo;
import com.ruijc.sevenzip.Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class OutArchive {

    private final Logger logger = LoggerFactory.getLogger(OutArchive.class);

    private RandomAccessOutputStream stream;
    private final OutBuffer outByte;
    private CRC crc;
    private long headerOffset;

    public OutArchive() {
        outByte = new OutBuffer();
        outByte.create(1 << 16);
    }

    private void writeBytes(OutputStream stream, byte[] data, int size) throws IOException {
        int processedSize = 0;
        while (size > 0) {
            int curSize = size < 0x0FFFFFFF ? size : 0x0FFFFFFF;

            stream.write(data, processedSize, curSize);
            processedSize += curSize;
            size -= curSize;
        }
    }


    private void writeDirect(byte[] data, int size) throws IOException {
        writeBytes(stream, data, size);
    }

    private static void setUInt32(byte[] p, int offset, int d) {
        for (int i = 0; i < 4; i++, d >>= 8)
            p[i + offset] = (byte) d;
    }

    private static void setUInt64(byte[] p, int offset, long d) {
        for (int i = 0; i < 8; i++, d >>= 8)
            p[i + offset] = (byte) d;
    }


    private void writeStartHeader(StartHeader h) throws IOException {
        byte[] buf = new byte[24];
        setUInt64(buf, 4, h.getNextHeaderOffset());
        setUInt64(buf, 12, h.getNextHeaderSize());
        setUInt32(buf, 20, h.getNextHeaderCRC());
        setUInt32(buf, 0, CRC.calculateDigest(buf, 4, 20));
        writeDirect(buf, 24);
    }

    private void writeBytes(byte[] data, int size) throws IOException {

        outByte.writeBytes(data, size);
        crc.update(data, size);

    }

    private void writeByte(Byte b) throws IOException {

        outByte.writeByte(b);
        crc.updateByte(b);

    }

    private void writeUInt32(int value) throws IOException {
        for (int i = 0; i < 4; i++) {
            writeByte((byte) value);
            value >>= 8;
        }
    }

    private void writeUInt64(long value) throws IOException {
        for (int i = 0; i < 8; i++) {
            writeByte((byte) value);
            value >>= 8;
        }
    }

    private void writeNumber(long value) throws IOException {
        byte firstByte = 0;
        byte mask = (byte) 0x80;
        int i;
        for (i = 0; i < 8; i++) {
            if (value < ((((long) (1)) << (7 * (i + 1))))) {
                firstByte |= ((byte) (value >> (8 * i)));
                break;
            }
            firstByte |= mask;
            mask >>= 1;
        }
        writeByte(firstByte);
        for (; i > 0; i--) {
            writeByte((byte) value);
            value >>= 8;
        }

    }

    private void writeFolder(Folder folder) throws IOException {
        writeNumber(folder.getCoders().size());
        int i;
        for (i = 0; i < folder.getCoders().size(); i++) {
            CoderInfo coder = folder.getCoders().get(i);

            int propsSize = coder.getProps().getCapacity();

            long id = coder.getMethodID();
            int idSize;
            for (idSize = 1; idSize < 8; idSize++) {
                if ((id >> (8 * idSize)) == 0) {
                    break;
                }
            }
            byte[] longID = new byte[15];
            for (int t = idSize - 1; t >= 0; t--, id >>= 8) {
                longID[t] = (byte) (id & 0xFF);
            }
            byte b;
            b = (byte) (idSize & 0xF);
            boolean isComplex = !coder.isSimpleCoder();
            b |= (isComplex ? 0x10 : 0);
            b |= ((propsSize != 0) ? 0x20 : 0);
            writeByte(b);
            writeBytes(longID, idSize);
            if (isComplex) {
                writeNumber(coder.getNumInStreams());
                writeNumber(coder.getNumOutStreams());
            }
            if (propsSize == 0) {
                continue;
            }
            writeNumber(propsSize);
            writeBytes(coder.getProps(), propsSize);
        }
    }

    private void writeBytes(ByteBuffer props, int propsSize) throws IOException {
        writeBytes(props.data(), propsSize);
    }

    private void writeBoolList(List<Boolean> boolList) throws IOException {
        byte b = 0;
        int mask = 0x80;
        for (Boolean aBoolList : boolList) {
            if (aBoolList)
                b |= mask;
            mask >>= 1;
            if (mask == 0) {
                writeByte(b);
                mask = 0x80;
                b = 0;
            }
        }
        if (mask != 0x80)
            writeByte(b);
    }


    private void writeHashDigests(List<Boolean> digestsDefined, List<Integer> digests) throws IOException {
        int numDefined = 0;
        int i;
        for (i = 0; i < digestsDefined.size(); i++)
            if (digestsDefined.get(i))
                numDefined++;
        if (numDefined == 0)
            return;

        writeByte((byte) Header.NID.kCRC);
        if (numDefined == digestsDefined.size())
            writeByte((byte) 1);
        else {
            writeByte((byte) 0);
            writeBoolList(digestsDefined);
        }
        for (i = 0; i < digests.size(); i++)
            if (digestsDefined.get(i))
                writeUInt32(digests.get(i));
    }

    private void writePackInfo(long dataOffset, List<Long> packSizes) throws IOException {
        if (packSizes.isEmpty()) {
            return;
        }
        writeByte((byte) Header.NID.kPackInfo);
        writeNumber(dataOffset);
        writeNumber(packSizes.size());
        writeByte((byte) Header.NID.kSize);
        for (Long packSize : packSizes) {
            writeNumber(packSize);
        }

        writeByte((byte) Header.NID.kEnd);
    }

    private void writeUnpackInfo(List<Folder> folders) throws IOException {
        if (folders.isEmpty())
            return;

        writeByte((byte) Header.NID.kUnPackInfo);

        writeByte((byte) Header.NID.kFolder);
        writeNumber(folders.size());
        {
            writeByte((byte) 0);
            for (Folder folder : folders) {
                writeFolder(folder);
            }
        }

        writeByte((byte) Header.NID.kCodersUnPackSize);
        for (Folder folder : folders) {
            for (int j = 0; j < folder.getUnpackSizes().size(); j++)
                writeNumber(folder.getUnpackSizes().get(j));
        }
        List<Boolean> unpackCRCsDefined = new ArrayList<Boolean>();
        List<Integer> unpackCRCs = new ArrayList<Integer>();
        unpackCRCsDefined.add(false);
        unpackCRCs.add(0);
        writeHashDigests(unpackCRCsDefined, unpackCRCs);

        writeByte((byte) Header.NID.kEnd);
    }

    private void writeSubStreamsInfo(
            List<Folder> folders,
            List<Integer> numUnpackStreamsInFolders,
            List<Long> unpackSizes,
            List<Boolean> digestsDefined,
            List<Integer> digests) throws IOException {
        writeByte((byte) Header.NID.kSubStreamsInfo);

        for (int i = 0; i < numUnpackStreamsInFolders.size(); i++) {
            if (numUnpackStreamsInFolders.get(i) != 1) {
                writeByte((byte) Header.NID.kNumUnPackStream);
                for (i = 0; i < numUnpackStreamsInFolders.size(); i++)
                    writeNumber(numUnpackStreamsInFolders.get(i));
                break;
            }
        }


        boolean needFlag = true;
        int index = 0;
        for (Integer numUnpackStreamsInFolder : numUnpackStreamsInFolders)
            for (int j = 0; j < numUnpackStreamsInFolder; j++) {
                if (j + 1 != numUnpackStreamsInFolder) {
                    if (needFlag)
                        writeByte((byte) Header.NID.kSize);
                    needFlag = false;
                    writeNumber(unpackSizes.get(index));
                }
                index++;
            }

        List<Boolean> digestsDefined2 = new ArrayList<Boolean>();
        List<Integer> digests2 = new ArrayList<Integer>();

        int digestIndex = 0;
        for (int i = 0; i < folders.size(); i++) {
            int numSubStreams = numUnpackStreamsInFolders.get(i);
            for (int j = 0; j < numSubStreams; j++, digestIndex++) {
                digestsDefined2.add(digestsDefined.get(digestIndex));
                digests2.add(digests.get(digestIndex));
            }
        }
        writeHashDigests(digestsDefined2, digests2);
        writeByte((byte) Header.NID.kEnd);
    }


    private static int bvGetSizeInBytes(List v) {
        return (v.size() + 7) / 8;
    }

    private void writeAlignedBoolHeader(List<Boolean> booleanList, int numDefined, int type, int itemSize) throws IOException {
        int bvSize = (numDefined == booleanList.size()) ? 0 : bvGetSizeInBytes(booleanList);
        long dataSize = (long) numDefined * itemSize + bvSize + 2;

        writeByte((byte) type);
        writeNumber(dataSize);
        if (numDefined == booleanList.size())
            writeByte((byte) 1);
        else {
            writeByte((byte) 0);
            writeBoolList(booleanList);
        }
        writeByte((byte) 0);
    }

    private void writeUInt64DefList(List<Boolean> defined, List<Long> values, int type) throws IOException {
        int numDefined = 0;

        int i;
        for (i = 0; i < defined.size(); i++)
            if (defined.get(i))
                numDefined++;

        if (numDefined == 0)
            return;

        writeAlignedBoolHeader(defined, numDefined, type, 8);

        for (i = 0; i < defined.size(); i++)
            if (defined.get(i))
                writeUInt64(values.get(i) * 10000 + 116444736000002500L);
    }

    private void writeHeader(ArchiveDatabase db) throws IOException {

        long packedSize = 0;
        for (int i = 0; i < db.getPackSizes().size(); i++)
            packedSize += db.getPackSizes().get(i);

        headerOffset = packedSize;

        writeByte((byte) Header.NID.kHeader);

        if (db.getFolders().size() > 0) {
            writeByte((byte) Header.NID.kMainStreamsInfo);
            writePackInfo(0, db.getPackSizes());

            writeUnpackInfo(db.getFolders());

            List<Long> unpackSizes = new ArrayList<Long>();
            List<Boolean> digestsDefined = new ArrayList<Boolean>();
            List<Integer> digests = new ArrayList<Integer>();
            for (int i = 0; i < db.getFiles().size(); i++) {
                FileItem file = db.getFiles().get(i);
                if (!file.hasStream())
                    continue;
                unpackSizes.add(file.getSize());
                digestsDefined.add(file.isCrcDefined());
                digests.add(file.getFileCRC());
            }

            writeSubStreamsInfo(
                    db.getFolders(),
                    db.getNumUnPackStreamsVector(),
                    unpackSizes,
                    digestsDefined,
                    digests);
            writeByte((byte) Header.NID.kEnd);
        }

        if (db.getFiles().isEmpty()) {
            writeByte((byte) Header.NID.kEnd);
            return;
        }

        writeByte((byte) Header.NID.kFilesInfo);
        writeNumber(db.getFiles().size());


        List<Boolean> emptyStreamList = new ArrayList<Boolean>();
        int numEmptyStreams = 0;
        for (int i = 0; i < db.getFiles().size(); i++)
            if (db.getFiles().get(i).hasStream())
                emptyStreamList.add(false);
            else {
                emptyStreamList.add(true);
                numEmptyStreams++;
            }
        if (numEmptyStreams > 0) {
            writeByte((byte) Header.NID.kEmptyStream);
            writeNumber(bvGetSizeInBytes(emptyStreamList));
            writeBoolList(emptyStreamList);

            List<Boolean> emptyFileList = new ArrayList<Boolean>();
            int numEmptyFiles = 0;
            for (int i = 0; i < db.getFiles().size(); i++) {
                FileItem file = db.getFiles().get(i);
                if (!file.hasStream()) {
                    emptyFileList.add(!file.isDirectory());
                    if (!file.isDirectory()) {
                        numEmptyFiles++;
                    }
                }
            }

            if (numEmptyFiles > 0) {
                writeByte((byte) Header.NID.kEmptyFile);
                writeNumber(bvGetSizeInBytes(emptyFileList));
                writeBoolList(emptyFileList);
            }
        }


        int numDefined = 0;
        int namesDataSize = 0;
        for (int i = 0; i < db.getFiles().size(); i++) {
            String name = db.getFiles().get(i).getName();
            if (!name.isEmpty())
                numDefined++;
            namesDataSize += (name.length() + 1) * 2;
        }

        if (numDefined > 0) {
            namesDataSize++;

            writeByte((byte) Header.NID.kName);
            writeNumber(namesDataSize);
            writeByte((byte) 0);
            for (int i = 0; i < db.getFiles().size(); i++) {
                String name = db.getFiles().get(i).getName();
                for (int t = 0; t < name.length(); t++) {
                    char c = name.charAt(t);
                    writeByte((byte) c);
                    writeByte((byte) (c >> 8));
                }
                writeByte((byte) 0);
                writeByte((byte) 0);
            }
        }


        writeUInt64DefList(db.getMTimesDefined(), db.getMTimes(), Header.NID.kLastWriteTime);

        List<Boolean> boolList = new ArrayList<Boolean>();
        numDefined = 0;
        for (int i = 0; i < db.getFiles().size(); i++) {
            boolean defined = db.getFiles().get(i).isAttributesDefined();
            boolList.add(defined);
            if (defined)
                numDefined++;
        }
        if (numDefined > 0) {
            writeAlignedBoolHeader(boolList, numDefined, (byte) Header.NID.kWinAttributes, 4);
            for (int i = 0; i < db.getFiles().size(); i++) {
                FileItem file = db.getFiles().get(i);
                if (file.isAttributesDefined())
                    writeUInt32(file.getAttributes());
            }
        }


        writeByte((byte) Header.NID.kEnd); // for files
        writeByte((byte) Header.NID.kEnd); // for headers
    }


    public void writeDatabase(ArchiveDatabase db) throws IOException {
        int headerCRC;
        long headerSize;
        if (db.isEmpty()) {
            headerSize = 0;
            headerOffset = 0;
            headerCRC = CRC.calculateDigest(null, 0);
        } else {
            outByte.setStream(stream);
            outByte.init();
            crc = new CRC();
            writeHeader(db);

            outByte.flush();
            headerCRC = crc.getDigest();
            headerSize = outByte.getProcessedSize();
        }
        StartHeader h = new StartHeader();
        h.setNextHeaderSize(headerSize);
        h.setNextHeaderCRC(headerCRC);
        h.setNextHeaderOffset(headerOffset);
        stream.seek(8, RandomAccessOutputStream.STREAM_SEEK_SET);
        writeStartHeader(h);

    }

    public void create(RandomAccessOutputStream stream) throws IOException {
        this.stream = stream;
        writeSignature();
    }

    private void writeSignature() throws IOException {
        stream.write(Header.kSignature);
    }

    public void skipPrefixArchiveHeader() {
        try {
            stream.seek(24, RandomAccessOutputStream.STREAM_SEEK_CUR);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
