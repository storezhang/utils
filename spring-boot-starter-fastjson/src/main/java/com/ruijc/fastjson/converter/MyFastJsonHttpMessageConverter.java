package com.ruijc.fastjson.converter;

import com.alibaba.fastjson.JSON;
import com.ruijc.fastjson.bean.FastJsonFilterObject;
import com.ruijc.fastjson.filter.SimpleSerializerFilter;
import com.ruijc.util.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 转换器
 *
 * @author Storezhang
 */
public class MyFastJsonHttpMessageConverter extends com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter {

    private static Charset UTF_8 = Charset.forName("UTF-8");

    public MyFastJsonHttpMessageConverter() {
        setSupportedMediaTypes(
                Arrays.asList(
                        new MediaType("application", "json", UTF_8),
                        new MediaType("application", "*+json", UTF_8),
                        new MediaType("application", "jsonp", UTF_8),
                        new MediaType("application", "*+jsonp", UTF_8)
                )
        );
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (obj instanceof FastJsonFilterObject) {
            FastJsonFilterObject jsonFilterObject = (FastJsonFilterObject) obj;
            OutputStream out = outputMessage.getBody();
            SimpleSerializerFilter simpleSerializerFilter = new SimpleSerializerFilter(jsonFilterObject.getIncludes(), jsonFilterObject.getExcludes());
            String text = JSON.toJSONString(jsonFilterObject.getData(), simpleSerializerFilter, getFastJsonConfig().getSerializerFeatures());
            String callback = jsonFilterObject.getCallback();
            if (!StringUtils.isBlank(callback)) {
                text = callback + "(" + text + ")";
            }
            byte[] bytes = getResult(text).getBytes(getFastJsonConfig().getCharset());
            out.write(bytes);
        } else {
            OutputStream out = outputMessage.getBody();
            String text = JSON.toJSONString(obj, getFastJsonConfig().getSerializerFeatures());
            byte[] bytes = getResult(text).getBytes(getFastJsonConfig().getCharset());
            out.write(bytes);
        }
    }

    protected String getResult(String old) {
        return old;
    }
}
