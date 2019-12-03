package com.framework.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.framework.common.annotation.DataFilter;
import com.framework.common.utils.Constant;
import com.framework.modules.sys.dao.SysDeptDao;
import com.framework.modules.sys.entity.SysDeptEntity;
import com.framework.modules.sys.service.SysDeptService;

@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
	
	@Autowired
	private  SysDeptDao dao;
	@Override
	@DataFilter(subDept = false, user = false,domain=false)
	public List<SysDeptEntity> queryList(Map<String, Object> params) {
		List<SysDeptEntity> deptList = this.selectList(new EntityWrapper<SysDeptEntity>().addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));

//		for (SysDeptEntity sysDeptEntity : deptList) {
//			SysDeptEntity parentDeptEntity = this.selectById(sysDeptEntity.getParentId());
//			if (parentDeptEntity != null) {
//				sysDeptEntity.setParentName(parentDeptEntity.getName());
//			}
//		}
		return deptList;
	}

	@Override
	public List<String> queryDetpIdList(String parentId) {
		return baseMapper.queryDetpIdList(parentId);
	}

	@Override
	public List<String> getSubDeptIdList(String deptId) {
		// 部门及子部门ID列表
		List<String> deptIdList = new ArrayList<>();

		// 获取子部门ID
		List<String> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		return deptIdList;
	}

	/**
	 * 递归
	 */
	private void getDeptTreeList(List<String> subIdList, List<String> deptIdList) {
		for (String deptId : subIdList) {
			List<String> list = queryDetpIdList(deptId);
			if (list.size() > 0) {
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}
	public SysDeptEntity selectById(String deptId){
		return dao.selectById(deptId);
	}
}
