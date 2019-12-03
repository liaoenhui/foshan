package com.framework.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 域维护表（域或者系统，比如桐庐事中事后管理平台、数据资产管理平台等）
 * 
 * @author MIT
 * @email framework@gmail.com
 * @date 2018-08-06 17:04:27
 */
@TableName("sys_domain")
public class SysDomainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 域ID（相当于业务系统id）
	 */
	@TableId
	private Integer domainId;
	/**
	 * 域名称（相当于业务系统名称）
	 */
	private String domainName;
	/**
	 * 域名称（相当于业务系统名称）
	 */
	@TableField(exist = false)
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 域的基础路径（basepath)  格式为：ip:port/webcontext
	 */
	private String basePath;
	/**
	 * 
	 */
	private String createUser;
	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 预留字段，暂时没用
	 */
	private Integer domainType;

	/**
	 * 设置：域ID（相当于业务系统id）
	 */
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	/**
	 * 获取：域ID（相当于业务系统id）
	 */
	public Integer getDomainId() {
		return domainId;
	}
	/**
	 * 设置：域ID（相当于业务系统id）
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
		this.name = domainName;
	}
	/**
	 * 获取：域ID（相当于业务系统id）
	 */
	public String getDomainName() {
		return domainName;
	}
	/**
	 * 设置：域的基础路径（basepath)  格式为：ip:port/webcontext
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	/**
	 * 获取：域的基础路径（basepath)  格式为：ip:port/webcontext
	 */
	public String getBasePath() {
		return basePath;
	}
	/**
	 * 设置：
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	/**
	 * 获取：
	 */
	public String getCreateUser() {
		return createUser;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：预留字段，暂时没用
	 */
	public void setDomainType(Integer domainType) {
		this.domainType = domainType;
	}
	/**
	 * 获取：预留字段，暂时没用
	 */
	public Integer getDomainType() {
		return domainType;
	}
}
