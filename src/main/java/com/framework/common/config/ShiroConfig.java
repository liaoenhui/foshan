package com.framework.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.framework.common.config.jwt.JWTNUPFilter;
import com.framework.modules.sys.shiro.JWTRealm;
import com.framework.modules.sys.shiro.RedisShiroSessionDAO;
import com.framework.modules.sys.shiro.UserRealm;


/**
 * Shiro的配置文件
 */
@Configuration
public class ShiroConfig {
	
	@Bean("sessionManager")
	public SessionManager sessionManager(RedisShiroSessionDAO redisShiroSessionDAO, @Value("${framework.redis.open}") boolean redisOpen, @Value("${framework.shiro.redis}") boolean shiroRedis) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session过期时间为1小时(单位：毫秒)，默认为30分钟
		sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdUrlRewritingEnabled(true);//初始值为false，测试跨域访问设置为true

		// 如果开启redis缓存且framework.shiro.redis=true，则shiro session存到redis里
		if (redisOpen && shiroRedis) {
			sessionManager.setSessionDAO(redisShiroSessionDAO);
		}
		return sessionManager;
	}
    

	@Bean("securityManager")
	public SecurityManager securityManager(UserRealm userRealm, JWTRealm jwtRealm,SessionManager sessionManager) {
		 DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//			        设置realm.
		securityManager.setAuthenticator(modularRealmAuthenticator());
        List<Realm> realms = new ArrayList<Realm>();
        //添加多个Realm
        realms.add(userRealm);
        realms.add(jwtRealm);
        securityManager.setRealms(realms);
//		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//		securityManager.setRealm(userRealm);
		securityManager.setSessionManager(sessionManager);

		return securityManager;
	}
	@Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        //自己重写的ModularRealmAuthenticator
        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);
		shiroFilter.setLoginUrl("/login.html");
		shiroFilter.setUnauthorizedUrl("/");

		// 添加自己的过滤器并且取名为jwt
        Map<String, Filter> fm = new HashMap<>();
//        fm.put("jwt", new JWTFilter());
        fm.put("jwtunp", new JWTNUPFilter());

        shiroFilter.setFilters(fm);
        
		Map<String, String> filterMap = new LinkedHashMap<>();
		filterMap.put("/swagger/**", "anon");
		filterMap.put("/v2/api-docs", "anon");
		filterMap.put("/swagger-ui.html", "anon");
		filterMap.put("/webjars/**", "anon");
		filterMap.put("/swagger-resources/**", "anon");

		filterMap.put("/statics/**", "anon");
		filterMap.put("/templates/modules/web-rsc/**", "anon");
		filterMap.put("/login.html", "anon");
		filterMap.put("/sys/to_other_login_page", "anon");
		filterMap.put("/sys/login", "anon");
		filterMap.put("/sys/loginRest", "anon");
		filterMap.put("/favicon.ico", "anon");
		filterMap.put("/captcha.jpg", "anon");
//		filterMap.put("/**/api/**", "jwt");
//		filterMap.put("/modules/sys/user.html", "jwt");
		filterMap.put("/**", "jwtunp");
		shiroFilter.setFilterChainDefinitionMap(filterMap);
//		org.apache.shiro.web.filter.authc.FormAuthenticationFilter
		return shiroFilter;
	}

	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
}
