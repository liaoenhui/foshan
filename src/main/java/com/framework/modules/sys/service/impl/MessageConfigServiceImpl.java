package com.framework.modules.sys.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.framework.common.annotation.DataFilter;
import com.framework.common.utils.Constant;
import com.framework.common.utils.PageUtils;
import com.framework.common.utils.Query;
import com.framework.common.utils.UuidUtil;
import com.framework.modules.sys.dao.MessageConfigDao;
import com.framework.modules.sys.dao.SysRoleDao;
import com.framework.modules.sys.entity.MessageConfigEntity;
import com.framework.modules.sys.entity.SysDeptEntity;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.entity.SysRoleEntity;
import com.framework.modules.sys.service.MessageConfigService;
import com.framework.modules.sys.service.SysDeptService;
import com.framework.modules.sys.service.SysDomainService;
import com.framework.modules.sys.service.SysRoleDeptService;
import com.framework.modules.sys.service.SysRoleMenuService;
import com.framework.modules.sys.service.SysRoleService;
import com.framework.modules.sys.service.SysUserRoleService;

/**
 * 角色
 */
@Service("messageConfigService")
public class MessageConfigServiceImpl extends ServiceImpl<MessageConfigDao, MessageConfigEntity> implements MessageConfigService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysDomainService sysDomainService;
	
	@Autowired
	private MessageConfigDao messageConfigDao;
	@Override
	@DataFilter(subDept = false, user = false,domain=false)
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String) params.get("roleName");

		Page<MessageConfigEntity> page = this.selectPage(new Query<MessageConfigEntity>(params).getPage(),
				new EntityWrapper<MessageConfigEntity>().addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));

		/*for (MessageConfigEntity sysRoleEntity : page.getRecords()) {
			if(sysRoleEntity.getDeptId() != null){
				SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysRoleEntity.getDeptId());
				if (sysDeptEntity != null) {
					sysRoleEntity.setDeptName(sysDeptEntity.getName());
				}
			}
			//todo 应该改为一对一关联，而不是把结果集查出来后再拿ID去查
			if(sysRoleEntity.getDomainId() != null){
				SysDomainEntity sysDomainEntity = sysDomainService.selectById(sysRoleEntity.getDomainId());
				if (sysDomainEntity != null) {
					sysRoleEntity.setDomainName(sysDomainEntity.getDomainName());
				}
			}
		}*/

		return new PageUtils(page);
	}
/*
	@Override
	@DataFilter(domain=true)
	public List<MessageConfigEntity> queryList(Map<String, Object> params) {
		String roleName = (String) params.get("roleName");
//		Long domainId = (Long) params.get("domainId");
		List<MessageConfigEntity> list = this.selectList(
				new EntityWrapper<MessageConfigEntity>().like(StringUtils.isNotBlank(roleName), "role_name", roleName).addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));

		return list;
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(MessageConfigEntity role) {
		role.setCreateTime(new Date());
		this.insert(role);

		// 保存角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		// 保存角色与部门关系
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {
		this.updateAllColumnById(role);

		// 更新角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		// 保存角色与部门关系
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		// 删除角色
		this.deleteBatchIds(Arrays.asList(roleIds));

		// 删除角色与菜单关联
		sysRoleMenuService.deleteBatch(roleIds);

		// 删除角色与部门关联
		sysRoleDeptService.deleteBatch(roleIds);

		// 删除角色与用户关联
		sysUserRoleService.deleteBatch(roleIds);
	}

	*/
	@Override
	public List<MessageConfigEntity> queryList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void saveApprove(MessageConfigEntity role) {
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> approvelist=role.getApproveList();
		List<String> projectlist=role.getProjectList();
		List<String> projectStagelist=role.getProjectStageList();
		//后添加
		map.put("CREATE_TIME", new Date());
		map.put("RECEIVER", role.getReceiver());
		map.put("SENDER", role.getSender());
		map.put("CC_ACCOUNT", role.getCcAccount());
		if(null!=approvelist && approvelist.size()>0) {
			map.put("list", approvelist);
			messageConfigDao.deleteApprove(map);
			for(int i=0;i<approvelist.size();i++) {
				map.put("ID", UuidUtil.get32UUID());
				map.put("SOURCE_ID", approvelist.get(i).toString());
				map.put("TYPE_ID", 2);
				messageConfigDao.saveApprove(map);
			}
		}
		if(null!=projectlist && projectlist.size()>0) {
			map.put("list", projectlist);
			messageConfigDao.deleteApprove(map);
			for(int i=0;i<projectlist.size();i++) {
				map.put("ID", UuidUtil.get32UUID());
				map.put("SOURCE_ID", projectlist.get(i).toString());
				map.put("TYPE_ID", 1);
				messageConfigDao.saveApprove(map);
			}
		}
		if(null!=projectStagelist && projectStagelist.size()>0) {
			map.put("list", projectStagelist);
			messageConfigDao.deleteApprove(map);
			for(int i=0;i<projectStagelist.size();i++) {
				map.put("ID", UuidUtil.get32UUID());
				map.put("SOURCE_ID", projectStagelist.get(i).toString());
				map.put("TYPE_ID", 4);
				messageConfigDao.saveApprove(map);
			}
		}
		
		
	}
	@Override
	public void update(MessageConfigEntity role) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteBatch(String[] roleIds) {
		this.deleteBatchIds(Arrays.asList(roleIds));
	}
	@Override
	public List<SysDeptEntity> findLastChildren(String deptId) {
		
		return messageConfigDao.findLastChildren(deptId);
	}
	@Override
	public List<SysDeptEntity> findProjectList() {
		return messageConfigDao.findProjectList();
	}
	@Override
	public List<SysDeptEntity> findLastChildrenForProject(String deptId) {
		return messageConfigDao.findLastChildrenForProject(deptId);
	}
	@Override
	public List<MessageConfigEntity> getConfigList(Map<String, Object> params) {
		return messageConfigDao.getConfigList(params);
	}
	@Override
	public void setReveiverById(Map<String, Object> params) {
		 messageConfigDao.setReveiverById(params);
	}
	@Override
	public Map<String, Object> getReceiverById(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return messageConfigDao.getReceiverById(params);
	}

}
