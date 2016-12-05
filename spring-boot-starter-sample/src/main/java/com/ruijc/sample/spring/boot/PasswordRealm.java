package com.ruijc.sample.spring.boot;

import com.ruijc.sample.spring.boot.bean.User;
import com.ruijc.sample.spring.boot.logic.UserL;
import com.ruijc.shiro.EncryptToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Shiro授权实现
 *
 * @author Storezhang
 */
public class PasswordRealm extends AuthorizingRealm {

    public static final String REALM_NAME = "PasswordRealm";
    @Autowired
    private UserL userL;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof EncryptToken;
    }

    @Override
    public String getName() {
        return REALM_NAME;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
        AuthenticationInfo info;

        EncryptToken token = (EncryptToken) at;
        String username = token.getUsername();
        User user = userL.getByUsername(username);
        if (null == user) {
            throw new UnknownAccountException();
        }

        info = new SimpleAuthenticationInfo(token.getUsername(), user.getPassword(), getName());

        return info;
    }
}
