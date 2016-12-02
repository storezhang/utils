package com.ruijc.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Shiro登录表单配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "shiro.auth")
public class ShiroAuthFilterProperties {

    private String userParamName;
    private String passwordParamName;
    private String rememberMeParamName;
    private String dataParamName;

    public ShiroAuthFilterProperties() {
        userParamName = "username";
        passwordParamName = "password";
        rememberMeParamName = "rememberMe";
        dataParamName = "data";
    }

    public String getUserParamName() {
        return userParamName;
    }

    public void setUserParamName(String userParamName) {
        this.userParamName = userParamName;
    }

    public String getPasswordParamName() {
        return passwordParamName;
    }

    public void setPasswordParamName(String passwordParamName) {
        this.passwordParamName = passwordParamName;
    }

    public String getRememberMeParamName() {
        return rememberMeParamName;
    }

    public void setRememberMeParamName(String rememberMeParamName) {
        this.rememberMeParamName = rememberMeParamName;
    }

    public String getDataParamName() {
        return dataParamName;
    }

    public void setDataParamName(String dataParamName) {
        this.dataParamName = dataParamName;
    }
}
