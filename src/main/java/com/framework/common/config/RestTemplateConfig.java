/**
 * 
 */
package com.framework.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: RestTemplateConfig
 * @Description:TODO
 * @author: linqing
 * @date: 2019年3月14日 下午5:26:28
 * 
 */
@Configuration
public class RestTemplateConfig {
	@Bean
//	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
