package com.ruijc.log;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 默认日志实现
 *
 * @author Storezhang
 */
public class BaseLogger implements ILogger {

    public void log(String store, String topic, String source, Object... args) {
    }

    public List<Map<String, Object>> query(String store, String topic, long start, long end, String query, long offset, int num) {
        return Collections.EMPTY_LIST;
    }
}
