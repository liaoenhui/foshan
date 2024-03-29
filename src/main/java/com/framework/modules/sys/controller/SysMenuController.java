package com.framework.modules.sys.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.annotation.SysLog;
import com.framework.common.exception.RRException;
import com.framework.common.utils.Constant;
import com.framework.common.utils.R;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.entity.SysMenuEntity;
import com.framework.modules.sys.entity.SysUserEntity;
import com.framework.modules.sys.service.SysDomainService;
import com.framework.modules.sys.service.SysMenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统菜单
 */
@RestController
@RequestMapping("/sys/menu")
@Api(tags = "权限菜单API")
public class SysMenuController  extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysDomainService sysDomainService;
	/**
	 * 导航菜单
	 */
	@RequestMapping(value={"/nav","/api/nav"}, method = RequestMethod.GET)
	@ApiOperation(value="导航菜单",notes="获取登陆用户的权限菜单")
	public R nav() {
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
		R r = R.ok().put("menuList", menuList);
		r.put("username", getUser().getUsername());
		return r;
	}

	/**
	 * 所有菜单列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public List<SysMenuEntity> list(@RequestParam Map<String, Object> params) {
		SysUserEntity user = getUser();
		params.put("domainId", user.getDomainId());
		List<SysMenuEntity> menuList = sysMenuService.queryList(params);
		for (SysMenuEntity sysMenuEntity : menuList) {
			SysMenuEntity parentMenuEntity = sysMenuService.selectById(sysMenuEntity.getParentId());
			if (parentMenuEntity != null) {
				sysMenuEntity.setParentName(parentMenuEntity.getName());
			}
			if(sysMenuEntity.getDomainId()!=null){
				SysDomainEntity domainEntity = sysDomainService.selectById(sysMenuEntity.getDomainId());
				if(domainEntity==null){
					if(parentMenuEntity==null) continue;
					if(parentMenuEntity.getDomainId()!=null){
						SysDomainEntity pDomainEntity = sysDomainService.selectById(parentMenuEntity.getDomainId());
						if(pDomainEntity!=null){
							sysMenuEntity.setDomainName(pDomainEntity.getDomainName());
							sysMenuEntity.setDomainId(pDomainEntity.getDomainId());
						}
					}
				}else{
					sysMenuEntity.setDomainName(domainEntity.getDomainName());
				}
			}
			
		}

		return menuList;
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select() {
		// 查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

		// 添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);

		return R.ok().put("menuList", menuList);
	}

	/**
	 * 菜单信息
	 */
	@RequestMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public R info(@PathVariable("menuId") Long menuId) {
		SysMenuEntity menu = sysMenuService.selectById(menuId);
		return R.ok().put("menu", menu);
	}

	/**
	 * 保存
	 */
	@SysLog("保存菜单")
	@RequestMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenuEntity menu) {
		// 数据校验
		verifyForm(menu);

		sysMenuService.insert(menu);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@RequestMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public R update(@RequestBody SysMenuEntity menu) {
		// 数据校验
		verifyForm(menu);

		sysMenuService.updateById(menu);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public R delete(long menuId) {
		if (menuId <= 31) {
			return R.error("系统菜单，不能删除");
		}

		// 判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
		if (menuList.size() > 0) {
			return R.error("请先删除子菜单或按钮");
		}

		sysMenuService.delete(menuId);

		return R.ok();
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu) {
		if (StringUtils.isBlank(menu.getName())) {
			throw new RRException("菜单名称不能为空");
		}

		if (menu.getParentId() == null) {
			throw new RRException("上级菜单不能为空");
		}

		// 菜单
		if (menu.getType() == Constant.MenuType.MENU.getValue()) {
			if (StringUtils.isBlank(menu.getUrl())) {
				throw new RRException("菜单URL不能为空");
			}
		}

		// 上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if (menu.getParentId() != 0) {
			SysMenuEntity parentMenu = sysMenuService.selectById(menu.getParentId());
			parentType = parentMenu.getType();
		}

		// 目录、菜单
		if (menu.getType() == Constant.MenuType.CATALOG.getValue() || menu.getType() == Constant.MenuType.MENU.getValue()) {
			if (parentType != Constant.MenuType.CATALOG.getValue()) {
				throw new RRException("上级菜单只能为目录类型");
			}
			return;
		}

		// 按钮
		if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
			if (parentType != Constant.MenuType.MENU.getValue()) {
				throw new RRException("上级菜单只能为菜单类型");
			}
			return;
		}
	}
}
