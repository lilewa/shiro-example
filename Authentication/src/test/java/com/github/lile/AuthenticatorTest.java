package com.github.lile;

import junit.framework.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Test;


/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-25
 * <p>Version: 1.0
 */
public class AuthenticatorTest {


    @Test
    public void testHelloworld() {

        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro.ini");

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");

        try {
            //4、登录，即身份验证
            subject.login(token);
        }catch ( UnknownAccountException uae ) {
            //username wasn't in the system, show them an error message?
        }catch ( IncorrectCredentialsException ice ) {
                //password didn't match, try again?
        }catch ( LockedAccountException lae ) {
                //account for that username is locked - can't login.  Show them a message?
        }  //... more types exceptions to check if you want ...
        catch ( AuthenticationException ae ) {
                //unexpected condition - error?
        }

       Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录

        //6、退出
        subject.logout();
    }


    //ModularRealmAuthenticator 默认使用 AtLeastOneSuccessfulStrategy 这个策略,
    /*
        MyRealm1 要求用户名必须是 zhang ，密码是 123
        MyRealm2 要求用户名必须是 wang ，密码是 123
        MyRealm3 要求用户名必须是 zhang ，密码是 123
        MyRealm4 要求用户名必须是 zhang ，密码是 123
    */
    @Test
    public void testAtLeastOneSuccessfulStrategyWithSuccess() {
        //这里使用 zhang ， 123  登陆。MyRealm1，MyRealm3成功，MyRealm2失败
        login("classpath:shiro-authenticator-atLeastOne-success.ini");
        Subject subject = SecurityUtils.getSubject();
        /*
         AuthenticationStrategy还负责聚合每个成功验证的结果, 并将其 "合并" 到AuthenticationInfo。
         AuthenticationInfo是Authenticator 实例认证后返回的结果 用来表示subject的身份
         subject.getPrincipals()== AuthenticationInfo.getPrincipals() 不确定??
        */
        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2, principalCollection.asList().size());
        System.out.println("isAuthenticated:"+subject.isAuthenticated());


    }

    @Test
    public void testAllSuccessfulStrategyWithSuccess() {

       //只配置了$myRealm1,$myRealm3
        login("classpath:shiro-authenticator-all-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2, principalCollection.asList().size());
        System.out.println("isAuthenticated:"+subject.isAuthenticated());
    }

    @Test(expected = UnknownAccountException.class)
    public void testAllSuccessfulStrategyWithFail() {
        //在MyRealm2的getAuthenticationInfo方法中会抛出UnknownAccountException异常
        login("classpath:shiro-authenticator-all-fail.ini");

    }




    //自定义Strategy
    @Test
    public void testAtLeastTwoStrategyWithSuccess() {
        //只配置了 $myRealm1,$myRealm2,$myRealm4

        login("classpath:shiro-authenticator-atLeastTwo-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，因为myRealm1和myRealm4返回的身份一样所以输出时只返回一个
       PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(1, principalCollection.asList().size());
        System.out.println("isAuthenticated:"+subject.isAuthenticated());
    }

    //自定义Strategy
    @Test
    public void testOnlyOneStrategyWithSuccess() {

        login("classpath:shiro-authenticator-onlyone-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，因为myRealm1和myRealm4返回的身份一样所以输出时只返回一个
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(1, principalCollection.asList().size());
        System.out.println("isAuthenticated:"+subject.isAuthenticated());
    }

    private void login(String configFile) {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory(configFile);

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");

        subject.login(token);
    }

    @After
    public void tearDown() throws Exception {
        ThreadContext.unbindSubject();//退出时请解除绑定Subject到线程 否则对下次测试造成影响
    }

}
