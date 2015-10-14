package com.xwkj.booking.bean;

import java.util.Date;

import com.xwkj.booking.domain.Pay;

public class PayBean {
	
	private String pid; 
	private Boolean payed;
	private Date payDate;
	
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
	public PayBean(Pay pay) {
		super();
		this.pid = pay.getPid();
		this.payed = pay.getPayed();
		this.payDate = pay.getPayDate();
	}
	
}
