package com.framework.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 角色
 */
@TableName("foshan.tb_supervision_receiver_config")
public class MessageConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private String id;
	private String typeId;
	private String sourceId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String flag;
	private String sender;
	private String receiver;
	private String ccAccount;
	@TableField(exist = false)
	private String sourcename;
	@TableField(exist = false)
	private String sendername;
	@TableField(exist = false)
	private String receivername;
	@TableField(exist = false)
	private String receivedept;
	public String getReceivedept() {
		return receivedept;
	}
	public void setReceivedept(String receivedept) {
		this.receivedept = receivedept;
	}
	@TableField(exist = false)
	private String ccAccountname;
	public String getSourcename() {
		return sourcename;
	}
	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	public String getSendername() {
		return sendername;
	}
	public void setSendername(String sendername) {
		this.sendername = sendername;
	}
	public String getReceivername() {
		return receivername;
	}
	public void setReceivername(String receivername) {
		this.receivername = receivername;
	}
	public String getCcAccountname() {
		return ccAccountname;
	}
	public void setCcAccountname(String ccAccountname) {
		this.ccAccountname = ccAccountname;
	}
	@TableField(exist = false)
	private List<String> approveList;
	@TableField(exist = false)
	private List<String> projectList;
	@TableField(exist = false)
	private List<String> projectStageList;
	public List<String> getProjectStageList() {
		return projectStageList;
	}
	public void setProjectStageList(List<String> projectStageList) {
		this.projectStageList = projectStageList;
	}
	public List<String> getProjectList() {
		return projectList;
	}
	public void setProjectList(List<String> projectList) {
		this.projectList = projectList;
	}
	public List<String> getApproveList() {
		return approveList;
	}
	public void setApproveList(List<String> approveList) {
		this.approveList = approveList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getCcAccount() {
		return ccAccount;
	}
	public void setCcAccount(String ccAccount) {
		this.ccAccount = ccAccount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
