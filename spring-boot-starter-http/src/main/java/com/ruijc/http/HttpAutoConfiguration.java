package com.ruijc.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Http客户端自动配置
 */
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
public class HttpAutoConfiguration {

    @Autowired
    private HttpProperties httpProperties;

    @Bean
    public HttpClient httpClient() {
        HttpClient client = HttpClient.instance();
        if (null != httpProperties) {
            if (0 != httpProperties.getConnectionTimeout()) {
                client.setConnectionTimeout(httpProperties.getConnectionTimeout());
            }
        }

        return client;
    }
}
