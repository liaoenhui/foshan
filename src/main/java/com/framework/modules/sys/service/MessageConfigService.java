package com.framework.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.framework.common.utils.PageUtils;
import com.framework.modules.sys.entity.MessageConfigEntity;
import com.framework.modules.sys.entity.SysDeptEntity;

/**
 * 角色
 */
public interface MessageConfigService extends IService<MessageConfigEntity> {

	PageUtils queryPage(Map<String, Object> params);

	List<MessageConfigEntity> queryList(Map<String, Object> params);
	
	void saveApprove(MessageConfigEntity role);

	void update(MessageConfigEntity role);

	void deleteBatch(String[] roleIds);

	List<SysDeptEntity> findLastChildren(String deptId);

	List<SysDeptEntity> findProjectList();

	List<SysDeptEntity> findLastChildrenForProject(String deptId);

	List<MessageConfigEntity> getConfigList(Map<String, Object> params);

	void setReveiverById(Map<String, Object> params);

	Map<String, Object> getReceiverById(Map<String, Object> params);

}
