package com.ruijc.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cookie配置项
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "shiro.cookie")
public class ShiroCookieProperties {

    private String name;
    private String value;
    private int maxAge;
    private int version;
    private boolean secure;
    private boolean httpOnly;
    private String cipherKey;
    private String encryptionCipherKey;
    private String decryptionCipherKey;

    public ShiroCookieProperties() {
        maxAge = 60 * 60 * 24 * 365;
        version = -1;
        name = "rememberMe";
        httpOnly = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String getCipherKey() {
        return cipherKey;
    }

    public void setCipherKey(String cipherKey) {
        this.cipherKey = cipherKey;
    }

    public String getEncryptionCipherKey() {
        return encryptionCipherKey;
    }

    public void setEncryptionCipherKey(String encryptionCipherKey) {
        this.encryptionCipherKey = encryptionCipherKey;
    }

    public String getDecryptionCipherKey() {
        return decryptionCipherKey;
    }

    public void setDecryptionCipherKey(String decryptionCipherKey) {
        this.decryptionCipherKey = decryptionCipherKey;
    }
}
