package com.xwkj.booking.bean;

import java.util.Date;

import com.xwkj.booking.domain.Pay;

public class PayBean {
	
	private String pid; 
	private Boolean payed;
	private Date payDate;
	private String payWay;
	private String wechatNonce;
	
	public String getPid() {
		return pid;
	}
	public Boolean getPayed() {
		return payed;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public void setPayed(Boolean payed) {
		this.payed = payed;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getPayWay() {
		return payWay;
	}
	public String getWechatNonce() {
		return wechatNonce;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public void setWechatNonce(String wechatNonce) {
		this.wechatNonce = wechatNonce;
	}
	
	public PayBean(Pay pay) {
		super();
		this.pid = pay.getPid();
		this.payed = pay.getPayed();
		this.payDate = pay.getPayDate();
		this.payWay = pay.getPayWay();
		this.wechatNonce = pay.getWechatNonce();
	}
	
}
