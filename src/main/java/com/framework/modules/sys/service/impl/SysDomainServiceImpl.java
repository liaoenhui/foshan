package com.framework.modules.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.framework.common.annotation.DataFilter;
import com.framework.common.utils.Constant;
import com.framework.common.utils.PageUtils;
import com.framework.common.utils.Query;
import com.framework.modules.sys.dao.SysDomainDao;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.service.SysDomainService;


@Service("sysDomainService")
public class SysDomainServiceImpl extends ServiceImpl<SysDomainDao, SysDomainEntity> implements SysDomainService {

    @Override
    @DataFilter(subDept = false, user = false,domain=false)
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysDomainEntity> page = this.selectPage(
                new Query<SysDomainEntity>(params).getPage(),
                getEntityWrapper(params)
        );
        return new PageUtils(page);
    }
    @Override
    @DataFilter(subDept = false, user = false,domain=false)
	public List<SysDomainEntity> queryList(Map<String, Object> params) {
		List<SysDomainEntity> domainList = this.selectList(getEntityWrapper(params));
		return domainList;
	}
    private Wrapper<SysDomainEntity> getEntityWrapper(Map<String, Object> params){
//    	Long domainId = (Long) params.get("domainId");
    	return  new EntityWrapper<SysDomainEntity>().addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
    }
}
