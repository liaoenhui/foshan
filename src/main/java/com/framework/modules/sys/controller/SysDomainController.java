package com.framework.modules.sys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.utils.PageUtils;
import com.framework.common.utils.R;
import com.framework.common.validator.ValidatorUtils;
import com.framework.modules.sys.entity.SysDomainEntity;
import com.framework.modules.sys.service.SysDomainService;



/**
 * 域维护表（域或者系统，比如桐庐事中事后管理平台、数据资产管理平台等）
 *
 * @author MIT
 * @email framework@gmail.com
 * @date 2018-08-06 17:04:27
 */
@RestController
@RequestMapping("sys/sysdomain")
public class SysDomainController extends AbstractController{
    @Autowired
    private SysDomainService sysDomainService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:sysdomain:list")
    public R list(@RequestParam Map<String, Object> params){
//    	SysUserEntity user = getUser();
//		params.put("domainId", user.getDomainId());
        PageUtils page = sysDomainService.queryPage(params);

        return R.ok().put("page", page);
    }
	/**
	 * 列表
	 */
	@RequestMapping("/listAll")
	@RequiresPermissions("sys:sysdomain:list")
	public List<SysDomainEntity> listAll(@RequestParam Map<String, Object> params) {
//		SysUserEntity user = getUser();
//		Map<String,Object> a = new  HashMap<String, Object>();
//		a.put("domainId", user.getDomainId());
		List<SysDomainEntity> domainList = sysDomainService.queryList(params);

		return domainList;
	}

    /**
     * 信息
     */
    @RequestMapping("/info/{domainId}")
    @RequiresPermissions("sys:sysdomain:info")
    public R info(@PathVariable("domainId") Integer domainId){
        SysDomainEntity sysDomain = sysDomainService.selectById(domainId);

        return R.ok().put("sysDomain", sysDomain);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:sysdomain:save")
    public R save(@RequestBody SysDomainEntity sysDomain){
        sysDomainService.insert(sysDomain);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:sysdomain:update")
    public R update(@RequestBody SysDomainEntity sysDomain){
        ValidatorUtils.validateEntity(sysDomain);
        sysDomainService.updateAllColumnById(sysDomain);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:sysdomain:delete")
    public R delete(@RequestBody Integer[] domainIds){
        sysDomainService.deleteBatchIds(Arrays.asList(domainIds));

        return R.ok();
    }

}
