package com.ruijc.shiro;

import javax.servlet.Filter;
import java.util.Map;

/**
 * 自定义Shiro的Filter
 *
 * @author Storezhang
 */
public interface ShiroFilterCustomizer {

    Map<String, Filter> customize(Map<String, Filter> filterMap);
}
