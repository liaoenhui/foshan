package com.framework.modules.sys.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.framework.common.utils.PageUtils;
import com.framework.common.utils.Query;
import com.framework.modules.sys.dao.SysLogDao;
import com.framework.modules.sys.dao.SysLogViewDao;
import com.framework.modules.sys.entity.SysLogEntity;
import com.framework.modules.sys.entity.SysLogViewEntity;
import com.framework.modules.sys.service.SysLogService;
import com.framework.modules.sys.service.SysLogViewService;

@Service("sysLogViewService")
public class SysLogViewServiceImpl extends ServiceImpl<SysLogViewDao, SysLogViewEntity> implements SysLogViewService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String key = (String) params.get("key");

		Page<SysLogViewEntity> page = this.selectPage(new Query<SysLogViewEntity>(params).getPage(), new EntityWrapper<SysLogViewEntity>().like(StringUtils.isNotBlank(key), "username", key));

		return new PageUtils(page);
	}
}
