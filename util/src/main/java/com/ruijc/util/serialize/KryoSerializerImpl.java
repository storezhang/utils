package com.ruijc.util.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ruijc.util.CollectionUtils;

import java.io.ByteArrayOutputStream;

public class KryoSerializerImpl<T> implements ISerializer<T> {

    private Kryo kryo;

    public KryoSerializerImpl() {
        kryo = new Kryo();
    }

    public <T> T deserialize(byte[] bytes) {
        T result;

        if (CollectionUtils.isBlank(bytes)) {
            return null;
        }

        try {
            result = (T) kryo.readObject(new Input(bytes), Object.class);
        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    public byte[] serialize(Object object) {
        byte[] result;

        if (object == null) {
            result = new byte[0];
            return result;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            Output output = new Output(stream);
            kryo.writeObject(output, object);
            output.close();
        } catch (Exception e) {
            result = new byte[0];
        }
        result = stream.toByteArray();

        return result;
    }
}
