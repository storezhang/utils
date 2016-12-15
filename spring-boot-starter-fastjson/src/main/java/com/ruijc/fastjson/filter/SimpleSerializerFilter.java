package com.ruijc.fastjson.filter;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.ruijc.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 过滤器
 *
 * @author Storezhang
 */
public class SimpleSerializerFilter extends SimplePropertyPreFilter {
    private Map<Class, HashSet<String>> includes;
    private Map<Class, HashSet<String>> excludes;

    public SimpleSerializerFilter(Map<Class, HashSet<String>> includes, Map<Class, HashSet<String>> excludes) {
        this.includes = includes;
        this.excludes = excludes;
    }

    @Override
    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (!CollectionUtils.isBlank(includes)) {
            for (Map.Entry<Class, HashSet<String>> include : includes.entrySet()) {
                Class objClass = include.getKey();
                Set<String> includeProp = include.getValue();
                if (objClass.isAssignableFrom(source.getClass())) {
                    return includeProp.contains(name);
                } else {
                    continue;
                }
            }
        }
        if (!CollectionUtils.isBlank(excludes)) {
            for (Map.Entry<Class, HashSet<String>> exclude : excludes.entrySet()) {
                Class objClass = exclude.getKey();
                Set<String> includeProp = exclude.getValue();
                if (objClass.isAssignableFrom(source.getClass())) {
                    return !includeProp.contains(name);
                } else {
                    continue;
                }
            }
        }

        return true;
    }
}
