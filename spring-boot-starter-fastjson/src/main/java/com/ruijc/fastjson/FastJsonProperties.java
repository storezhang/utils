package com.ruijc.fastjson;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijc.fastjson.converter.FastJsonHttpMessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("spring.http.converters.fastjson")
public class FastJsonProperties {

    private List<SerializerFeature> features;
    private Class<? extends com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter> converter;

    public FastJsonProperties() {
        converter = FastJsonHttpMessageConverter.class;
    }

    public List<SerializerFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<SerializerFeature> features) {
        this.features = features;
    }

    public Class<? extends com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter> getConverter() {
        return converter;
    }

    public void setConverter(Class<? extends com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter> converter) {
        this.converter = converter;
    }
}
