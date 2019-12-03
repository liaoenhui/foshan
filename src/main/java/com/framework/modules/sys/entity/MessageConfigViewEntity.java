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
@TableName("foshan.v_supervision_receiver_config")
public class MessageConfigViewEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private String id;
	private String typeid;
	private String sourceid;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	private String flag;
	private String sender;
	private String receiver;
	private String ccaccount;
	private String sourcename;
	private String sendername;
	private String receivername;
	private String receivedept;
	private String issendphone;
	private String issendphonevalue;
	public String getIssendphone() {
		return issendphone;
	}
	public void setIssendphone(String issendphone) {
		this.issendphone = issendphone;
	}
	public String getIssendphonevalue() {
		return issendphonevalue;
	}
	public void setIssendphonevalue(String issendphonevalue) {
		this.issendphonevalue = issendphonevalue;
	}
	public String getReceivedept() {
		return receivedept;
	}
	public void setReceivedept(String receivedept) {
		this.receivedept = receivedept;
	}
	private String ccaccountname;
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
	public String getCcaccountname() {
		return ccaccountname;
	}
	public void setCcaccountname(String ccaccountname) {
		this.ccaccountname = ccaccountname;
	}
	@TableField(exist = false)
	private List<String> approveList;
	@TableField(exist = false)
	private List<String> projectList;
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
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getSourceid() {
		return sourceid;
	}
	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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
	public String getCcaccount() {
		return ccaccount;
	}
	public void setCcaccount(String ccaccount) {
		this.ccaccount = ccaccount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
