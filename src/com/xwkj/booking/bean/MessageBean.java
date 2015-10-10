package com.xwkj.booking.bean;

import java.util.Date;

import com.xwkj.booking.domain.Message;

public class MessageBean {
	private String mid;
	private String name;
	private String email;
	private String telephone;
	private String content;
	private Date date;
	private boolean looked;
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isLooked() {
		return looked;
	}
	public void setLooked(boolean looked) {
		this.looked = looked;
	}
	
	public MessageBean(Message message) {
		super();
		this.mid = message.getMid();
		this.name = message.getName();
		this.email = message.getEmail();
		this.telephone = message.getTelephone();
		this.content = message.getContent();
		this.date = message.getDate();
		this.looked = message.getLooked();
	}
}
