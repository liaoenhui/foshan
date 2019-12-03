package com.framework.modules.sys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
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
import com.framework.modules.sys.dao.SysUserDao;
import com.framework.modules.sys.entity.SysDeptEntity;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.entity.SysUserEntity;
import com.framework.modules.sys.service.SysDeptService;
import com.framework.modules.sys.service.SysDomainService;
import com.framework.modules.sys.service.SysUserRoleService;
import com.framework.modules.sys.service.SysUserService;
import com.framework.modules.sys.shiro.ShiroUtils;

/**
 * 系统用户
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysDomainService sysDomainService;
	@Autowired
	private SysUserDao sysUserDao;
	@Override
	public List<Long> queryAllMenuId(String userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	@DataFilter(subDept = false, user = false,domain=false)
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String) params.get("username");
//		Long domainId = (Long) params.get("domainId");

		Page<SysUserEntity> page = this.selectPage(new Query<SysUserEntity>(params).getPage(),
				new EntityWrapper<SysUserEntity>().like(StringUtils.isNotBlank(username), "username", username).addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));

		for (SysUserEntity sysUserEntity : page.getRecords()) {
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysUserEntity.getDeptId());
			sysUserEntity.setDeptName(sysDeptEntity.getName().replaceAll(" ", ""));
			if(sysUserEntity.getDomainId()!=null){
				SysDomainEntity sysDomainEntity = sysDomainService.selectById(sysUserEntity.getDomainId());
				sysUserEntity.setDomainName(sysDomainEntity.getDomainName());
			}
		}

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		// sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
		this.insert(user);

		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysUserEntity user) {
		if (StringUtils.isBlank(user.getPassword())) {
			user.setPassword(null);
		} else {
			user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
		}
		this.updateById(user);

		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public boolean updatePassword(String userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity, new EntityWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
	}

	@Override
	public List<Map<String, Object>> getUserList() {
		return sysUserDao.getUserList();
	}

	/* 
	 * 
	 */
	@Override
	public List<Map<String, Object>> getLocationListByCode(String locationCode) {
		// TODO Auto-generated method stub
		return sysUserDao.getLocationListByCode(locationCode);
	}

}
