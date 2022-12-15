package com.sms.common.shiro;


import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {

    @Resource
    private MyRealmByPassword myRealmByPassword;
    @Resource
    private MyCredentialsMatcherByPassword myCredentialsMatcherByPassword;

    @Resource
    private MyRealmByCode myRealmByCode;
    @Resource
    private MyCredentialsMatcherByCode myCredentialsMatcherByCode;

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        //账号密码
        myRealmByPassword.setCredentialsMatcher(myCredentialsMatcherByPassword);
        //验证码
        myRealmByCode.setCredentialsMatcher(myCredentialsMatcherByCode);
        //2 创建认证对象，并设置认证策略
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        manager.setAuthenticator(modularRealmAuthenticator);
        //封装list
        List<Realm> realmList = new LinkedList<>();
        realmList.add(myRealmByPassword);
        realmList.add(myRealmByCode);
        manager.setRealms(realmList);
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager, JwtFilter jwtFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filter = new HashMap<>();
        //添加过滤器
        filter.put("jwt", jwtFilter);
        shiroFilterFactoryBean.setFilters(filter);
        //按顺序执行，链式
        Map<String, String> filterMap = new LinkedHashMap<>();
        //添加页面对应的过滤器
        //不需要认证
        filterMap.put("/swagger-ui.html**", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/user/login", "anon");
        filterMap.put("/user/code/login", "anon");
        filterMap.put("/mail/send", "anon");
        //需要认证
        filterMap.put("/**", "jwt");
        shiroFilterFactoryBean.setLoginUrl("/user/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public JwtFilter getJwtFilter() {
        return new JwtFilter();
    }

}
