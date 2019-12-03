package com.framework.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.framework.common.utils.PageUtils;
import com.framework.modules.sys.entity.SysDomainEntity;

/**
 * 域维护表（域或者系统，比如桐庐事中事后管理平台、数据资产管理平台等）
 *
 * @author MIT
 * @email framework@gmail.com
 * @date 2018-08-06 17:04:27
 */
public interface SysDomainService extends IService<SysDomainEntity> {

    PageUtils queryPage(Map<String, Object> params);
	List<SysDomainEntity> queryList(Map<String, Object> map);

}

