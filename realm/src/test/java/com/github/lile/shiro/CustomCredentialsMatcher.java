package com.github.lile.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import java.lang.reflect.Array;

//类中除了equals方法都和SimpleCredentialsMatcher一致，只是为了测试自定义CredentialsMatcher
public class CustomCredentialsMatcher implements CredentialsMatcher {


    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Object tokenCredentials = getCredentials(token);
        Object accountCredentials = getCredentials(info);
        return equals(tokenCredentials, accountCredentials);
    }


    protected Object getCredentials(AuthenticationToken token) {
        return token.getCredentials();
    }


    protected Object getCredentials(AuthenticationInfo info) {
        return info.getCredentials();
    }

    //自定义
    protected boolean equals(Object tokenCredentials, Object accountCredentials) {

        char[]  token= (char[])tokenCredentials;
        String strToken= String.valueOf(token);
        String accoutn=String.valueOf(accountCredentials);
        boolean result=  strToken.equals(accoutn);
        return result;

    }
    //SimpleCredentialsMatcher 的比较实现
   /* protected boolean equals(Object tokenCredentials, Object accountCredentials) {
        if (log.isDebugEnabled()) {
            log.debug("Performing credentials equality check for tokenCredentials of type [" +
                    tokenCredentials.getClass().getName() + " and accountCredentials of type [" +
                    accountCredentials.getClass().getName() + "]");
        }
        if (isByteSource(tokenCredentials) && isByteSource(accountCredentials)) {
            if (log.isDebugEnabled()) {
                log.debug("Both credentials arguments can be easily converted to byte arrays.  Performing " +
                        "array equals comparison");
            }
            byte[] tokenBytes = toBytes(tokenCredentials);
            byte[] accountBytes = toBytes(accountCredentials);
            return Arrays.equals(tokenBytes, accountBytes);
        } else {
            return accountCredentials.equals(tokenCredentials);
        }
    }*/
}
