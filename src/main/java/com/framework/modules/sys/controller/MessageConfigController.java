package com.framework.modules.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.framework.common.annotation.SysLog;
import com.framework.common.utils.Constant;
import com.framework.common.utils.PageUtils;
import com.framework.common.utils.Query;
import com.framework.common.utils.R;
import com.framework.common.validator.ValidatorUtils;
import com.framework.modules.sys.entity.MessageConfigEntity;
import com.framework.modules.sys.entity.SysDeptEntity;
import com.framework.modules.sys.entity.SysRoleEntity;
import com.framework.modules.sys.service.MessageConfigService;
import com.framework.modules.sys.service.MessageConfigViewService;
import com.framework.modules.sys.service.SysRoleDeptService;
import com.framework.modules.sys.service.SysRoleMenuService;
import com.framework.modules.sys.service.SysRoleService;

/**
 * 短信配置管理
 */
@RestController
@RequestMapping("/message/config")
public class MessageConfigController extends AbstractController {
	@Autowired
	private MessageConfigService messageConfigService;
	@Autowired
	private MessageConfigViewService messageConfigViewService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;

	/**
	 * 短信配置列表
	 */
	@SysLog("查看短信配置列表")
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = messageConfigViewService.queryPage(params);
		return R.ok().put("page", page);
	}
	/**
	 * 列表
	 */
	@RequestMapping("/findLastChildren")
	public R findLastChildren(@RequestParam Map<String, Object> params) {
		String deptId=params.get("deptId")+"";
		List<SysDeptEntity> deptList = messageConfigService.findLastChildren(deptId);
		return R.ok().put("deptList", deptList);
	}
	/**
	 * 列表
	 */
	@RequestMapping("/findLastChildrenForProject")
	public R findLastChildrenForProject(@RequestParam Map<String, Object> params) {
		String deptId=params.get("deptId")+"";
		List<SysDeptEntity> deptList = messageConfigService.findLastChildrenForProject(deptId);
		return R.ok().put("deptList", deptList);
	}
	/**
	 * 保存信息配置
	 */
	@SysLog("新增短信配置")
	@RequestMapping("/save")
	public R save(@RequestBody MessageConfigEntity entity) {
		ValidatorUtils.validateEntity(entity);
		messageConfigService.saveApprove(entity);

		return R.ok();
	}
	/**
	 * 删除信息配置
	 */
	@SysLog("删除信息配置")
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] roleIds) {
		messageConfigService.deleteBatch(roleIds);
		return R.ok();
	}
	/**
	 * 删除信息配置
	 */
	@SysLog("设置信息是否接受配置")
	@RequestMapping("/setReceiverById")
	public R setReceiverById(@RequestParam Map<String, Object> params) {
		messageConfigService.setReveiverById(params);
		return R.ok();
	}
	@RequestMapping("/getReceiverById")
	public R getReceiverById(@RequestParam Map<String, Object> params) {
		Map<String, Object> receiverInfo = messageConfigService.getReceiverById(params);
		return R.ok().put("receiverInfo", receiverInfo);
	}
	/**
	 * 项目列表
	 */
	@RequestMapping("/projectList")
	public List<SysDeptEntity> projectList() {
		List<SysDeptEntity> projectList = messageConfigService.findProjectList();

		return projectList;
	}
	
	
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select(@RequestParam Map<String, Object> params) {
//		SysUserEntity user = getUser();
//		params.put("domainId", user.getDomainId());
		List<MessageConfigEntity> list = messageConfigService.queryList(params);

		return R.ok().put("list", list);
	}

	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId) {
		MessageConfigEntity role = messageConfigService.selectById(roleId);

		/*// 查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		// 查询角色对应的部门
		List<String> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[] { roleId });
		role.setDeptIdList(deptIdList);*/

		return R.ok().put("role", role);
	}



	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public R update(@RequestBody MessageConfigEntity role) {
		ValidatorUtils.validateEntity(role);

		messageConfigService.update(role);

		return R.ok();
	}

	
}
