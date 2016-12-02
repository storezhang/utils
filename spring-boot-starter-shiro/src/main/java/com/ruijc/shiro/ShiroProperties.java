package com.ruijc.shiro;

import org.apache.shiro.realm.Realm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.servlet.Filter;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private final Ehcache ehcache;
    private Class<? extends Realm> realmClass;
    private Class<? extends AuthFilter> authClass;
    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;
    private String hashAlgorithmName;
    private int hashIterations;
    private int maxRetry;
    private Map<String, Class<? extends Filter>> filters;
    private Map<String, String> filterChainDefinitions;
    private String filterChainSql;

    public ShiroProperties() {
        ehcache = new Ehcache();
        realmClass = null;
        authClass = AuthFilter.class;
        loginUrl = "/login";
        successUrl = "/index";
        unauthorizedUrl = "/unauthorized";
        hashAlgorithmName = "MD5";
        hashIterations = 1;
        maxRetry = 5;
    }

    public Class<? extends Realm> getRealmClass() {
        return realmClass;
    }

    public void setRealmClass(Class<? extends Realm> realmClass) {
        this.realmClass = realmClass;
    }

    public Class<? extends AuthFilter> getAuthClass() {
        return authClass;
    }

    public void setAuthClass(Class<? extends AuthFilter> authClass) {
        this.authClass = authClass;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getHashAlgorithmName() {
        return hashAlgorithmName;
    }

    public void setHashAlgorithmName(String hashAlgorithmName) {
        this.hashAlgorithmName = hashAlgorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Map<String, Class<? extends Filter>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Class<? extends Filter>> filters) {
        this.filters = filters;
    }

    public Map<String, String> getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(Map<String, String> filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public String getFilterChainSql() {
        return filterChainSql;
    }

    public void setFilterChainSql(String filterChainSql) {
        this.filterChainSql = filterChainSql;
    }

    public Ehcache getEhcache() {
        return ehcache;
    }

    public static class Ehcache {
        private String configFile;

        public Ehcache() {
            configFile = "classpath:org/apache/shiro/cache/ehcache/ehcache.xml";
        }

        public String getConfigFile() {
            return configFile;
        }

        public void setConfigFile(String configFile) {
            this.configFile = configFile;
        }
    }
}
