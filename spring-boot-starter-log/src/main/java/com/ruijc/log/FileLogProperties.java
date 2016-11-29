package com.ruijc.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * 文件系统日志配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "log.file")
public class FileLogProperties {

    private String path;

    @PostConstruct
    public void init() {
        path = "./log";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
