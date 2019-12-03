package com.framework.modules.job.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import com.framework.common.exception.RRException;
import com.framework.common.utils.SpringContextUtils;

/**
 * 执行定时任务
 */
public class ScheduleRunnable implements Runnable {
	private Object target;
	private Method method;
	private String params;
	private String url;

	public ScheduleRunnable(String beanName, String methodName,String url, String params) throws NoSuchMethodException, SecurityException {
		this.target = SpringContextUtils.getBean(beanName);
		this.params = params;
		this.url = url;

//		if (StringUtils.isNotBlank(params)) {
//			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
//		} else {
//			this.method = target.getClass().getDeclaredMethod(methodName);
//		}
		if (StringUtils.isNotBlank(params)) {
			this.method = target.getClass().getDeclaredMethod(methodName, String.class,String.class);
		} else {
			this.method = target.getClass().getDeclaredMethod(methodName,String.class);
		}
	}

	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(method);
			if (StringUtils.isNotBlank(params)) {
				method.invoke(target,url, params);
			} else {
				method.invoke(target,url);
			}
		} catch (Exception e) {
			throw new RRException("执行定时任务失败", e);
		}
	}

}
