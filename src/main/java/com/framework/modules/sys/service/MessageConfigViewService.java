package com.framework.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.framework.common.utils.PageUtils;
import com.framework.modules.sys.entity.MessageConfigEntity;
import com.framework.modules.sys.entity.MessageConfigViewEntity;
import com.framework.modules.sys.entity.SysDeptEntity;

/**
 * 角色
 */
public interface MessageConfigViewService extends IService<MessageConfigViewEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
