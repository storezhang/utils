/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.log;

import java.util.List;
import java.util.Map;

/**
 * 日志接口
 *
 * @author Storezhang
 */
public interface ILogger {

    /**
     * 写入日志
     *
     * @param store  日志存放的地方（如文件名、阿里云的存入LogStore等）
     * @param topic  日志类型（主题）
     * @param source 日志来源
     * @param args   日志参数，注意：请成对给出Key=Value的值
     */
    public void log(String store, String topic, String source, Object... args);

    public List<Map<String, Object>> query(String store, String topic, long start, long end, String query, long offset, int num);
}
