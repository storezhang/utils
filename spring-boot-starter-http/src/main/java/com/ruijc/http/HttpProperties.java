package com.ruijc.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@ConfigurationProperties("http")
public class HttpProperties {

    private int connectionTimeout;

    @PostConstruct
    public void init() {
        connectionTimeout = 0;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
