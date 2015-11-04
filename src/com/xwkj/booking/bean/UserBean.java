package com.xwkj.booking.bean;

import java.util.Date;

import com.xwkj.booking.domain.User;

public class UserBean {
	
	private String uid;
	private String telephone;
	private String uname;
	private String password;
	private Date registerDate;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	public UserBean(User user) {
		super();
		this.uid = user.getUid();
		this.telephone = user.getTelephone();
		this.uname = user.getUname();
		this.password = user.getPassword();
		this.registerDate = user.getRegisterDate();
	}
}
