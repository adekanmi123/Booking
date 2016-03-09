package com.xwkj.booking.domain;

import java.io.Serializable;
import java.util.Date;

public class Pay implements Serializable {
	
	private static final long serialVersionUID = -6762692634719686100L;
	
	private String pid; 
	private Boolean payed;
	private Date payDate;
	private String sign;
	private String payWay;
	private String wechatNonce;
	private Booking booking;
	
	public String getPid() {
		return pid;
	}
	public Boolean getPayed() {
		return payed;
	}
	public Date getPayDate() {
		return payDate;
	}
	public String getSign() {
		return sign;
	}
	public Booking getBooking() {
		return booking;
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
	public void setSign(String sign) {
		this.sign = sign;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
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
	
}
