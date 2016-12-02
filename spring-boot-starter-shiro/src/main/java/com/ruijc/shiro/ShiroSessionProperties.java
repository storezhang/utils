package com.ruijc.shiro;

import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Session配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "shiro.session")
public class ShiroSessionProperties {

    private long globalSessionTimeout;
    private boolean deleteInvalidSessions;
    private long validationInterval;
    private boolean validationSchedulerEnabled;
    private String activeSessionsCacheName;
    private Class<? extends SessionIdGenerator> idGenerator;

    public ShiroSessionProperties() {
        globalSessionTimeout = 30 * 60 * 1000L;
        deleteInvalidSessions = true;
        validationInterval = 60 * 60 * 1000L;
        validationSchedulerEnabled = true;
        activeSessionsCacheName = "shiro-activeSessionCache";
        idGenerator = JavaUuidSessionIdGenerator.class;
    }

    public long getGlobalSessionTimeout() {
        return globalSessionTimeout;
    }

    public void setGlobalSessionTimeout(long globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
    }

    public boolean isDeleteInvalidSessions() {
        return deleteInvalidSessions;
    }

    public void setDeleteInvalidSessions(boolean deleteInvalidSessions) {
        this.deleteInvalidSessions = deleteInvalidSessions;
    }

    public long getValidationInterval() {
        return validationInterval;
    }

    public void setValidationInterval(long validationInterval) {
        this.validationInterval = validationInterval;
    }

    public boolean isValidationSchedulerEnabled() {
        return validationSchedulerEnabled;
    }

    public void setValidationSchedulerEnabled(boolean validationSchedulerEnabled) {
        this.validationSchedulerEnabled = validationSchedulerEnabled;
    }

    public String getActiveSessionsCacheName() {
        return activeSessionsCacheName;
    }

    public void setActiveSessionsCacheName(String activeSessionsCacheName) {
        this.activeSessionsCacheName = activeSessionsCacheName;
    }

    public Class<? extends SessionIdGenerator> getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(Class<? extends SessionIdGenerator> idGenerator) {
        this.idGenerator = idGenerator;
    }
}
