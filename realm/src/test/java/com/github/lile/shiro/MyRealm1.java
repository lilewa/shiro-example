package com.github.lile.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import static java.awt.SystemColor.info;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-25
 * <p>Version: 1.0
 */
public class MyRealm1 extends AuthenticatingRealm {


    public String getName() {
        return "myrealm1";
    }

     public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken; //仅支持UsernamePasswordToken类型的Token
    }

    //重写doGetAuthenticationInfo(token)从特定数据源得到相应帐户数据。
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        return new SimpleAuthenticationInfo("zhang","123",getName());
    }



/*
  AuthorizingRealm抽象类中的getAuthenticationInfo(token) 方法是一个final方法，它为以认证过程提供固定模板
  public final AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        AuthenticationInfo info = getCachedAuthenticationInfo(token);
        if (info == null) {
            //otherwise not cached, perform the lookup:
            info = doGetAuthenticationInfo(token);
            log.debug("Looked up AuthenticationInfo [{}] from doGetAuthenticationInfo", info);
            if (token != null && info != null) {
                cacheAuthenticationInfoIfPossible(token, info);
            }
        } else {
            log.debug("Using cached authentication info [{}] to perform credentials matching.", info);
        }

        if (info != null) {
            assertCredentialsMatch(token, info);
        } else {
            log.debug("No AuthenticationInfo found for submitted AuthenticationToken [{}].  Returning null.", token);
        }

        return info;
    }*/
}
