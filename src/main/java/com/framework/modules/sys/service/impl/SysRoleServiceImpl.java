package com.framework.modules.sys.service.impl;

import java.util.Arrays;
import java.util.Date;
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
import com.framework.modules.sys.dao.SysRoleDao;
import com.framework.modules.sys.entity.SysDeptEntity;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.entity.SysRoleEntity;
import com.framework.modules.sys.service.SysDeptService;
import com.framework.modules.sys.service.SysDomainService;
import com.framework.modules.sys.service.SysRoleDeptService;
import com.framework.modules.sys.service.SysRoleMenuService;
import com.framework.modules.sys.service.SysRoleService;
import com.framework.modules.sys.service.SysUserRoleService;

/**
 * 角色
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
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
	private SysRoleDao sysRoleDao;
	@Override
	@DataFilter(subDept = false, user = false,domain=false)
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String) params.get("roleName");

		Page<SysRoleEntity> page = this.selectPage(new Query<SysRoleEntity>(params).getPage(),
				new EntityWrapper<SysRoleEntity>().like(StringUtils.isNotBlank(roleName), "role_name", roleName).addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));

		for (SysRoleEntity sysRoleEntity : page.getRecords()) {
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
		}

		return new PageUtils(page);
	}

	@Override
	@DataFilter(domain=false)
	public List<SysRoleEntity> queryList(Map<String, Object> params) {
		String roleName = (String) params.get("roleName");
//		Long domainId = (Long) params.get("domainId");
		List<SysRoleEntity> list = this.selectList(
				new EntityWrapper<SysRoleEntity>().like(StringUtils.isNotBlank(roleName), "role_name", roleName).addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));

		return list;
	}
	@Override
	public List<SysRoleEntity> queryListBySql(Map<String, Object> params) {
		return sysRoleDao.queryListBySql(params);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysRoleEntity role) {
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

	

}
