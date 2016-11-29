package com.ruijc.fastjson.advice;

import com.ruijc.fastjson.annotation.JSONP;
import com.ruijc.fastjson.annotation.MoreSerializeField;
import com.ruijc.fastjson.annotation.MultiSerializeField;
import com.ruijc.fastjson.annotation.SerializeField;
import com.ruijc.fastjson.bean.FastJsonFilterObject;
import com.ruijc.fastjson.exception.IncludeAndExcludeConflictException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 拦截器
 *
 * @author Storezhang
 */
@Order(1)
@ControllerAdvice
@ConditionalOnWebApplication
public class FastJsonResponseBodyAdvice implements ResponseBodyAdvice {

    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        FastJsonFilterObject jsonFilterObject = new FastJsonFilterObject();

        if (o == null) {
            return null;
        }

        if (!methodParameter.getMethod().isAnnotationPresent(JSONP.class)
                && !methodParameter.getMethod().isAnnotationPresent(SerializeField.class)
                && !methodParameter.getMethod().isAnnotationPresent(MultiSerializeField.class)
                && !methodParameter.getMethod().isAnnotationPresent(MoreSerializeField.class)) {
            return o;
        }

        if (methodParameter.getMethod().isAnnotationPresent(JSONP.class)) {
            Object obj = methodParameter.getMethod().getAnnotation(JSONP.class);
            handleAnnotation(JSONP.class, obj, jsonFilterObject);
        }
        if (methodParameter.getMethod().isAnnotationPresent(SerializeField.class)) {
            Object obj = methodParameter.getMethod().getAnnotation(SerializeField.class);
            handleAnnotation(SerializeField.class, obj, jsonFilterObject);
        }
        if (methodParameter.getMethod().isAnnotationPresent(MultiSerializeField.class)) {
            Object obj = methodParameter.getMethod().getAnnotation(MultiSerializeField.class);
            handleAnnotation(MultiSerializeField.class, obj, jsonFilterObject);
        }
        if (methodParameter.getMethod().isAnnotationPresent(MoreSerializeField.class)) {
            MoreSerializeField moreSerializeField = methodParameter.getMethod().getAnnotation(MoreSerializeField.class);
            SerializeField[] serializeFields = moreSerializeField.value();
            if (serializeFields.length > 0) {
                for (int i = 0; i < serializeFields.length; i++) {
                    handleAnnotation(SerializeField.class, serializeFields[i], jsonFilterObject);
                }
            }
        }

        jsonFilterObject.setData(o);

        return jsonFilterObject;
    }

    private void handleAnnotation(Class clazz, Object obj, FastJsonFilterObject jsonFilterObject) {
        String[] includes = {};
        String[] excludes = {};
        Class objClass = null;
        if (clazz.equals(JSONP.class)) {
            JSONP jsonp = (JSONP) obj;
            jsonFilterObject.setCallback(jsonp.value());
        }
        if (clazz.equals(SerializeField.class)) {
            SerializeField serializeField = (SerializeField) obj;
            includes = serializeField.includes();
            excludes = serializeField.excludes();
            objClass = serializeField.clazz();
        }
        if (clazz.equals(MultiSerializeField.class)) {
            MultiSerializeField multiSerializeField = (MultiSerializeField) obj;
            includes = multiSerializeField.includes();
            excludes = multiSerializeField.excludes();
            objClass = multiSerializeField.clazz();
        }
        if (includes.length > 0 && excludes.length > 0) {
            throw new IncludeAndExcludeConflictException("Can not use both include field and exclude field in an annotation!");
        } else if (includes.length > 0) {
            jsonFilterObject.getIncludes().put(objClass, new HashSet<String>(Arrays.asList(includes)));
        } else if (excludes.length > 0) {
            jsonFilterObject.getExcludes().put(objClass, new HashSet<String>(Arrays.asList(excludes)));
        }
    }
}
