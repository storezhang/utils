package com.ruijc.util.serialize;

import com.ruijc.util.CollectionUtils;

import java.io.*;

public class JavaSerializerImpl<T> implements ISerializer<T> {

    public <T> T deserialize(byte[] bytes) {
        T result;

        if (CollectionUtils.isBlank(bytes)) {
            result = null;
            return result;
        }

        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
            result = (T) objectInputStream.readObject();
        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    public byte[] serialize(Object object) {
        byte[] result;

        if (null == object) {
            result = new byte[0];
            return result;
        }

        if (!(object instanceof Serializable)) {
            result = new byte[0];
            return result;
        }

        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            result = byteStream.toByteArray();
        } catch (Exception ex) {
            result = new byte[0];
        }

        return result;
    }
}
