package com.ruijc.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitMatcher extends SimpleCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;
    private int maxRetry;
    @Autowired
    private PasswordService passwordSvc;

    public RetryLimitMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
        maxRetry = 5;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        boolean match = false;

        String username = (String) token.getPrincipal();
        AtomicInteger retryCount = passwordRetryCache.get(username);

        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > maxRetry) {
            return match;
        }

        boolean matches;
        if (token instanceof EncryptToken && !((EncryptToken) token).isEncrypted()) {
            matches = token.getCredentials().equals(info.getCredentials());
        } else {
            matches = passwordSvc.passwordsMatch(token.getCredentials(), info.getCredentials().toString());
        }

        if (matches) {
            passwordRetryCache.remove(username);
            match = true;
        }

        return match;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }
}
