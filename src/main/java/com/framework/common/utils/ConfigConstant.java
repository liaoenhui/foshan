package com.framework.common.utils;

/**
 * 系统参数相关Key
 */
public class ConfigConstant {
	/**
	 * 云存储配置KEY
	 */
	public final static String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";
	public final static Integer LOGIN_CODE_SUCCESS = 0;
	public final static Integer LOGIN_CODE_INFO_ERROR = 100;
	public final static Integer LOGIN_CODE_ACCOUNT_LOCKED = 200;
	public final static Integer LOGIN_CODE_CAPTCHA_FAILED = 300;
	public final static Integer LOGIN_CODE_OTHER_ERROR = 500;
	
	public final static String LOGIN_MSG_SUCCESS = "success";
	public final static String LOGIN_MSG_INFO_ERROR = "账号或密码不正确";
	public final static String LOGIN_MSG_ACCOUNT_LOCKED = "账号已被锁定,请联系管理员";
	public final static String LOGIN_MSG_CAPTCHA_FAILED = "账户验证失败";
	public final static String LOGIN_MSG_OTHER_ERROR = "未知异常，请联系管理员";
	
}
