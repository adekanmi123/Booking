package com.xwkj.booking.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String bid;
	private String bno;
	private Date checkin;
	private Date checkout;
	private Integer days;
	private Double amount;
	private Date createDate;
	private Boolean pay;
	private Boolean timeout;
	private Boolean stayed;
	private Room room;
	private Set<History> histories;
	private User user;
	
	public String getBid() {
		return bid;
	}
	public String getBno() {
		return bno;
	}
	public Date getCheckin() {
		return checkin;
	}
	public Date getCheckout() {
		return checkout;
	}
	public Integer getDays() {
		return days;
	}
	public Double getAmount() {
		return amount;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Boolean getPay() {
		return pay;
	}
	public Boolean getTimeout() {
		return timeout;
	}
	public void setTimeout(Boolean timeout) {
		this.timeout = timeout;
	}
	public Boolean getStayed() {
		return stayed;
	}
	public Room getRoom() {
		return room;
	}
	public Set<History> getHistories() {
		return histories;
	}
	public User getUser() {
		return user;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public void setBno(String bno) {
		this.bno = bno;
	}
	public void setCheckin(Date checkin) {
		this.checkin = checkin;
	}
	public void setCheckout(Date checkout) {
		this.checkout = checkout;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setPay(Boolean pay) {
		this.pay = pay;
	}
	public void setStayed(Boolean stayed) {
		this.stayed = stayed;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public void setHistories(Set<History> histories) {
		this.histories = histories;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
