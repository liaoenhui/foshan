package com.framework.modules.sys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.annotation.SysLog;
import com.framework.common.utils.JWTUtil;
import com.framework.common.utils.PageUtils;
import com.framework.common.utils.R;
import com.framework.common.utils.UuidUtil;
import com.framework.common.validator.Assert;
import com.framework.common.validator.ValidatorUtils;
import com.framework.common.validator.group.AddGroup;
import com.framework.common.validator.group.UpdateGroup;
import com.framework.modules.sys.entity.SysDeptEntity;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.entity.SysUserEntity;
import com.framework.modules.sys.service.SysDeptService;
import com.framework.modules.sys.service.SysDomainService;
import com.framework.modules.sys.service.SysUserRoleService;
import com.framework.modules.sys.service.SysUserService;
import com.framework.modules.sys.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = "登陆用户API")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysDomainService sysDomainService;
	
	@RequestMapping("/hasPermission")
	@ApiOperation(value="查询用户是否有某权限",notes="获取当前登陆用户信息格式：{code:0,msg:'true（有）/false(无）'}")
	public R hasPermission(String permission){
		Subject subject = SecurityUtils.getSubject();
		return R.ok().put("msg", (subject != null && subject.isPermitted(permission))+"");
	}
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params) {
		SysUserEntity user = getUser();
		params.put("domainId", user.getDomainId());
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/userList")
	public R userList() {
		List<Map<String, Object>> userList = sysUserService.getUserList();

		return R.ok().put("userList", userList);
	}
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping(value={"/info","/api/info"}, method = RequestMethod.GET)
	@ApiOperation(value="获取用户信息",notes="获取当前登陆用户信息")
	public R info() {
		SysUserEntity sue = getUser();
		if(sue!=null){
			SysDeptEntity sde = sysDeptService.selectById(sue.getDeptId());
			if(sde!=null) sue.setDeptName(sde.getName());
			SysDomainEntity sdoe = sysDomainService.selectById(sue.getDomainId());
			if(sdoe!=null) sue.setDomainName(sdoe.getDomainName());
			// 获取用户所属的角色列表
			List<Long> roleIdList = sysUserRoleService.queryRoleIdList(sue.getUserId());
			sue.setRoleIdList(roleIdList);
		}
		return R.ok().put("user", sue);
	}

	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/password")
	@ApiOperation(value="修改密码",notes="修改密码，并返回新token")
	public R password(String password, String newPassword) {
		Assert.isBlank(newPassword, "新密码不为能空");

		// 原密码
		password = ShiroUtils.sha256(password, getUser().getSalt());
		// 新密码
		newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());

		// 更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if (!flag) {
			return R.error("原密码不正确");
		}

		return R.ok().put("token", JWTUtil.sign(getUser().getUsername(), newPassword));
	}

	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") String userId) {
		SysUserEntity user = sysUserService.selectById(userId);

		// 获取用户所属的角色列表
		try {
			List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
			user.setRoleIdList(roleIdList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return R.ok().put("user", user);
	}
	
	@RequestMapping("/getLocationListByCode/{locationCode}")
	public R getLocationListByCode(@PathVariable("locationCode") String locationCode) {
		List<Map<String, Object>> result = sysUserService.getLocationListByCode(locationCode);
		return R.ok().put("result", result);
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user) {
		ValidatorUtils.validateEntity(user, AddGroup.class);
		user.setUserId(UuidUtil.get32UUID());
		sysUserService.save(user);

		return R.ok();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user) {
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		sysUserService.update(user);

		return R.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds) {
		if (ArrayUtils.contains(userIds, 1L)) {
			return R.error("系统管理员不能删除");
		}

		if (ArrayUtils.contains(userIds, getUserId())) {
			return R.error("当前用户不能删除");
		}

		sysUserService.deleteBatchIds(Arrays.asList(userIds));

		return R.ok();
	}
}
