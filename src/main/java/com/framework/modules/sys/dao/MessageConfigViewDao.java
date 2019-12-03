package com.framework.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.framework.modules.sys.entity.MessageConfigEntity;
import com.framework.modules.sys.entity.MessageConfigViewEntity;
import com.framework.modules.sys.entity.SysDeptEntity;

/**
 * 角色管理
 */
@Mapper
public interface MessageConfigViewDao extends BaseMapper<MessageConfigViewEntity> {


}
