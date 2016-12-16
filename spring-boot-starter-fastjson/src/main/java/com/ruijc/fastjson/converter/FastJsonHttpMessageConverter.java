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
public class FastJsonHttpMessageConverter extends com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter {

    private static Charset UTF_8 = Charset.forName("UTF-8");

    public FastJsonHttpMessageConverter() {
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
    protected final void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();
        if (obj instanceof FastJsonFilterObject) {
            FastJsonFilterObject filterObject = (FastJsonFilterObject) obj;
            String text = toJSONString(filterObject);
            byte[] bytes = getResult(text).getBytes(getFastJsonConfig().getCharset());
            out.write(bytes);
        } else {
            String text = JSON.toJSONString(obj, getFastJsonConfig().getSerializerFeatures());
            byte[] bytes = getResult(text).getBytes(getFastJsonConfig().getCharset());
            out.write(bytes);
        }
    }

    protected String toJSONString(FastJsonFilterObject filterObject) {
        SimpleSerializerFilter filter = new SimpleSerializerFilter(filterObject.getIncludes(), filterObject.getExcludes());
        String text = JSON.toJSONString(filterObject.getData(), filter, getFastJsonConfig().getSerializerFeatures());
        String callback = filterObject.getCallback();
        if (!StringUtils.isBlank(callback)) {
            text = callback + "(" + text + ")";
        }

        return text;
    }

    protected String getResult(String old) {
        return old;
    }
}
