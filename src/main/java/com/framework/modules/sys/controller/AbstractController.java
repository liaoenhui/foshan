package com.framework.modules.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.common.utils.JWTUtil;
import com.framework.modules.sys.dao.SysUserDao;
import com.framework.modules.sys.entity.SysUserEntity;

/**
 * Controller公共组件
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SysUserDao sysUserDao;
	protected SysUserEntity getUser() {
		if(SecurityUtils.getSubject().getPrincipal() instanceof SysUserEntity){
			return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
		}else{
			String token = (String) SecurityUtils.getSubject().getPrincipal();
			return getUser(JWTUtil.getUsername(token));
		}
	}
	 private SysUserEntity getUser(String username){
	   	 SysUserEntity user = new SysUserEntity();
			user.setUsername(username);
			user = sysUserDao.selectOne(user);
			return user;
	   }
	protected String getUserId() {
		return getUser().getUserId();
	}

	protected String getDeptId() {
		return getUser().getDeptId();
	}
}
