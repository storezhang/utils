package com.ruijc.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ruijc.fastjson.converter.MyFastJsonHttpMessageConverter;
import com.ruijc.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * FastJson自动化配置
 *
 * @author Storezhang
 */
@Configuration
@ConditionalOnClass(name = "com.alibaba.fastjson.JSON")
public class FastJsonAutoConfiguration {

    @Configuration
    @ConditionalOnClass(name = {"com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter"})
    @ConditionalOnProperty(name = {"spring.http.converters.preferred-json-mapper"}, havingValue = "fastjson", matchIfMissing = true)
    @ConditionalOnWebApplication
    @EnableConfigurationProperties(FastJsonProperties.class)
    protected static class FastJson2HttpMessageConverterConfiguration {

        @Autowired
        private FastJsonProperties properties;

        @Bean
        @ConditionalOnMissingBean(com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter.class)
        public HttpMessageConverters customConverters(FastJsonHttpMessageConverter converter) {
            Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

            if (null == converter) {
                Class<?> converterClass = properties.getConverter();
                converter = (FastJsonHttpMessageConverter) BeanUtils.instantiate(converterClass);
            }

            FastJsonConfig config = new FastJsonConfig();
            List<SerializerFeature> features = properties.getFeatures();
            if (!CollectionUtils.isBlank(features)) {
                SerializerFeature[] featureArray = new SerializerFeature[features.size()];
                config.setSerializerFeatures(features.toArray(featureArray));
            }

            converter.setFastJsonConfig(config);
            messageConverters.add(converter);

            return new HttpMessageConverters(true, messageConverters);
        }

        @Bean
        @ConditionalOnMissingBean(FastJsonHttpMessageConverter.class)
        public FastJsonHttpMessageConverter converter() {
            return new MyFastJsonHttpMessageConverter();
        }
    }
}
