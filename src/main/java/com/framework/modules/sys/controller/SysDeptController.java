package com.framework.modules.sys.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.utils.Constant;
import com.framework.common.utils.R;
import com.framework.modules.sys.entity.SysDeptEntity;
import com.framework.modules.sys.service.SysDeptService;

/**
 * 部门管理
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:dept:list")
	public List<SysDeptEntity> list() {
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

		return deptList;
	}

	/**
	 * 选择部门(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:dept:select")
	public R select() {
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

		// 添加一级部门
		if (getUserId() == Constant.SUPER_ADMIN) {
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId("0");
			root.setName("一级部门");
			root.setParentId("-1");
			root.setOpen(true);
			deptList.add(root);
		}

		return R.ok().put("deptList", deptList);
	}

	/**
	 * 上级部门Id(管理员则为0)
	 */
	@RequestMapping("/info")
	@RequiresPermissions("sys:dept:list")
	public R info() {
		String deptId = "0";
		if (getUserId() != Constant.SUPER_ADMIN) {
			List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
			String parentId = null;
//			for (SysDeptEntity sysDeptEntity : deptList) {
//				if (parentId == null) {
//					parentId = sysDeptEntity.getParentId();
//					continue;
//				}
//
//				if (Long.parseLong(parentId) > Long.parseLong(sysDeptEntity.getParentId())) {
//					parentId = sysDeptEntity.getParentId();
//				}
//			}
			deptId = parentId;
		}

		return R.ok().put("deptId", deptId);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{deptId}")
	@RequiresPermissions("sys:dept:info")
	public R info(@PathVariable("deptId") Long deptId) {
		SysDeptEntity dept = sysDeptService.selectById(deptId);

		return R.ok().put("dept", dept);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:dept:save")
	public R save(@RequestBody SysDeptEntity dept) {
		sysDeptService.insert(dept);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:dept:update")
	public R update(@RequestBody SysDeptEntity dept) {
		sysDeptService.updateById(dept);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:dept:delete")
	public R delete(String deptId) {
		// 判断是否有子部门
		List<String> deptList = sysDeptService.queryDetpIdList(deptId);
		if (deptList.size() > 0) {
			return R.error("请先删除子部门");
		}

		sysDeptService.deleteById(deptId);

		return R.ok();
	}

}
