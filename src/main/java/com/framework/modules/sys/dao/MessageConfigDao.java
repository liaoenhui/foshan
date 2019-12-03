package com.framework.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.framework.modules.sys.entity.MessageConfigEntity;
import com.framework.modules.sys.entity.SysDeptEntity;

/**
 * 角色管理
 */
@Mapper
public interface MessageConfigDao extends BaseMapper<MessageConfigEntity> {

	List<SysDeptEntity> findLastChildren(String deptId);

	void saveApprove(Map<String, Object> map);

	void deleteApprove(Map<String, Object> map);

	List<SysDeptEntity> findProjectList();

	List<SysDeptEntity> findLastChildrenForProject(String deptId);

	List<MessageConfigEntity> getConfigList(Map<String, Object> params);

	void setReveiverById(Map<String, Object> params);

	Map<String, Object> getReceiverById(Map<String, Object> params);

}
