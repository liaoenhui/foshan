package com.framework.modules.sys.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.framework.common.utils.PageUtils;
import com.framework.modules.sys.entity.SysLogEntity;
import com.framework.modules.sys.entity.SysLogViewEntity;

/**
 * 系统日志
 */
public interface SysLogViewService extends IService<SysLogViewEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
