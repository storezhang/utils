package com.ruijc.log;

import com.ruijc.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FastJson自动化配置
 *
 * @author Storezhang
 */
@Configuration
public class LogAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(name = {"log.type"}, havingValue = "file", matchIfMissing = true)
    @EnableConfigurationProperties(FileLogProperties.class)
    protected static class FileLogConfiguration {

        @Autowired
        private FileLogProperties properties;

        @Bean
        public ILogger fileLogger() {
            ILogger logger = new FileLogger(properties.getPath());

            return logger;
        }
    }
}
