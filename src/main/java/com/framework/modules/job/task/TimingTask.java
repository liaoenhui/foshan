/**
 * 
 */
package com.framework.modules.job.task;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**   
 * @ClassName:  TimingTask   
 * @Description:TODO
 * @author: linqing 
 * @date:   2019年3月12日 上午10:26:20   
 *     
 */
@Component("timingTask")
public class TimingTask {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private RestTemplate restTemplate;
	
	public void GET(String url) {
		restTemplate.getForObject(url, String.class);
	}
	public void POST(String url,String params) {
		HttpHeaders headers = new HttpHeaders(); // http请求头
//		Map<String,Object> body = Maps.newHashMap();
		Map body = (Map) JSONObject.parse(params);  
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 请求头设置属性  
		HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<Map<String,Object>>(body,headers);
		Map<String, Object> object = restTemplate.postForObject(url, requestEntity, Map.class);
	}
	
	/**
	 * get请求
	 */
//	public void GET(String url) {
//		HttpUtil.doGet(url);
//	}
	/**
	 * post请求(用于key-value格式的参数) 
	 */
//	public void postForMap(String url,Map map) {
//		HttpUtil.doPost(url, map);
//	}
	/**
	 * post请求（用于请求json格式的参数）
	 */
//	public void POST(String url,String params) {
//		try {
//			HttpUtil.doPost(url, params);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//		}
//	}
}
