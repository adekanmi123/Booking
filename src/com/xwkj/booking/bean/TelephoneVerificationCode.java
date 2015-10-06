package com.xwkj.booking.bean;

import java.util.Date;

public class TelephoneVerificationCode {
	
	private String telephone;
	private String code;
	private Date time;
	public String getTelephone() {
		return telephone;
	}
	public String getCode() {
		return code;
	}
	public Date getTime() {
		return time;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public TelephoneVerificationCode(String telephone, String code, Date time) {
		super();
		this.telephone = telephone;
		this.code = code;
		this.time = time;
	}

}
