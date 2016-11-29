package com.ruijc.util.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijc.util.CollectionUtils;

public class FastjsonSerializerImpl<T> implements ISerializer<T> {

    public <T> T deserialize(byte[] bytes) {
        T result;

        if (CollectionUtils.isBlank(bytes)) {
            return null;
        }

        try {
            result = (T) JSON.parse(bytes);
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

        try {
            result = JSON.toJSONBytes(object, SerializerFeature.WriteClassName);
        } catch (Exception e) {
            result = new byte[0];
        }

        return result;
    }
}
