package com.ruijc.shiro;

import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Shiro JDBC配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "shiro.realm.jdbc")
public class ShiroJdbcRealmProperties {

    private boolean enabled;
    private JdbcRealm.SaltStyle salt;
    private String authenticationQuery;
    private String userRolesQuery;
    private String permissionsQuery;
    private boolean permissionsLookupEnabled;

    public ShiroJdbcRealmProperties() {
        permissionsLookupEnabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthenticationQuery() {
        return authenticationQuery;
    }

    public void setAuthenticationQuery(String authenticationQuery) {
        this.authenticationQuery = authenticationQuery;
    }

    public String getUserRolesQuery() {
        return userRolesQuery;
    }

    public void setUserRolesQuery(String userRolesQuery) {
        this.userRolesQuery = userRolesQuery;
    }

    public String getPermissionsQuery() {
        return permissionsQuery;
    }

    public void setPermissionsQuery(String permissionsQuery) {
        this.permissionsQuery = permissionsQuery;
    }

    public JdbcRealm.SaltStyle getSalt() {
        return salt;
    }

    public void setSalt(JdbcRealm.SaltStyle salt) {
        this.salt = salt;
    }

    public boolean isPermissionsLookupEnabled() {
        return permissionsLookupEnabled;
    }

    public void setPermissionsLookupEnabled(boolean permissionsLookupEnabled) {
        this.permissionsLookupEnabled = permissionsLookupEnabled;
    }
}
