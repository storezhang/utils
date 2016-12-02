package com.ruijc.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 增强版UsernamePassword授权令牌
 * @author Storezhang
 */
public class EncryptToken extends UsernamePasswordToken {

    private boolean encrypted;

    public EncryptToken(String username, String password) {
        super(username, password);
    }

    public EncryptToken(String username, char[] password, String host) {
        super(username, password, host);
    }

    public EncryptToken(String username, String password, String host) {
        super(username, password, host);
    }

    public EncryptToken(String username, char[] password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public EncryptToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public EncryptToken(String username, char[] password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    public EncryptToken(String username, String password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    public EncryptToken() {
        this("", "");
    }

    public EncryptToken(String username, char[] password) {
        super(username, password);
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
