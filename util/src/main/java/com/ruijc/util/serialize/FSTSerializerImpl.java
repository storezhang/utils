package com.ruijc.util.serialize;

import com.ruijc.util.CollectionUtils;
import org.nustaq.serialization.FSTConfiguration;

public class FSTSerializerImpl<T> implements ISerializer<T> {

    private FSTConfiguration conf;

    public FSTSerializerImpl() {
        conf = FSTConfiguration.createDefaultConfiguration();
        conf.setForceSerializable(true);
    }

    public <T> T deserialize(byte[] bytes) {
        T result;

        if (CollectionUtils.isBlank(bytes)) {
            return null;
        }

        result = (T) conf.asObject(bytes);

        return result;
    }

    public byte[] serialize(Object object) {
        byte[] result;

        if (object == null) {
            result = new byte[0];
            return result;
        }

        result = conf.asByteArray(object);

        return result;
    }
}
