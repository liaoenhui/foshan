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
import com.framework.modules.sys.dao.MessageConfigViewDao;
import com.framework.modules.sys.dao.SysRoleDao;
import com.framework.modules.sys.entity.MessageConfigEntity;
import com.framework.modules.sys.entity.MessageConfigViewEntity;
import com.framework.modules.sys.entity.SysDeptEntity;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.entity.SysRoleEntity;
import com.framework.modules.sys.service.MessageConfigService;
import com.framework.modules.sys.service.MessageConfigViewService;
import com.framework.modules.sys.service.SysDeptService;
import com.framework.modules.sys.service.SysDomainService;
import com.framework.modules.sys.service.SysRoleDeptService;
import com.framework.modules.sys.service.SysRoleMenuService;
import com.framework.modules.sys.service.SysRoleService;
import com.framework.modules.sys.service.SysUserRoleService;

/**
 * 角色
 */
@Service("messageConfigViewService")
public class MessageConfigViewServiceImpl extends ServiceImpl<MessageConfigViewDao, MessageConfigViewEntity> implements MessageConfigViewService {
	
	@Override
	@DataFilter(subDept = false, user = false,domain=false)
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String) params.get("roleName");

		Page<MessageConfigViewEntity> page = this.selectPage(new Query<MessageConfigViewEntity>(params).getPage(),
				new EntityWrapper<MessageConfigViewEntity>().where("1=1", null).like(StringUtils.isNotBlank(roleName), "sourcename", roleName).addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)).orderBy("createtime",false));

		

		return new PageUtils(page);
	}

}
