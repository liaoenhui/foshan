package com.framework.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.framework.modules.sys.entity.SysRoleEntity;

/**
 * 角色管理
 */
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

	public List<SysRoleEntity> queryListBySql(Map<String, Object> params);
}
