package com.framework.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.framework.modules.sys.entity.SysUserEntity;

/**
 * 系统用户
 */
public interface SysUserDao extends BaseMapper<SysUserEntity> {

	/**
	 * 查询用户的所有权限
	 * 
	 * @param userId 用户ID
	 */
	List<String> queryAllPerms(String userId);

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(String userId);
	
	List<Map<String, Object>> getUserList();
	
	List<Map<String, Object>> getLocationListByCode(String locationCode);
	
}
