package com.framework.common.config;

import javax.servlet.DispatcherType;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.framework.common.config.jwt.JWTFilter;
import com.framework.common.xss.XssFilter;

/**
 * Filter配置
 */
@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean shiroFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		// 该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
		registration.addInitParameter("targetFilterLifecycle", "true");
		registration.setEnabled(true);
		registration.setOrder(Integer.MAX_VALUE - 1);
		registration.addUrlPatterns("/*");
		return registration;
	}

	@Bean
	public FilterRegistrationBean xssFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(new XssFilter());
		registration.addUrlPatterns("/*");
		registration.setName("xssFilter");
		registration.setOrder(Integer.MAX_VALUE);
		return registration;
	}
	
	@Bean
	public JWTFilter jwtFilterRegistration(){
		return new JWTFilter();
	}
}
