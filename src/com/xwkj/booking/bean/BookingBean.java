package com.xwkj.booking.bean;

import java.util.Date;

import com.xwkj.booking.domain.Booking;

public class BookingBean {
	
	private String bid;
	private String bno;
	private Date checkin;
	private Date checkout;
	private int days;
	private double amount;
	private Date createDate;
	private boolean stayed;
	private RoomBean room;
	private UserBean user;
	
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBno() {
		return bno;
	}
	public void setBno(String bno) {
		this.bno = bno;
	}
	public Date getCheckin() {
		return checkin;
	}
	public void setCheckin(Date checkin) {
		this.checkin = checkin;
	}
	public Date getCheckout() {
		return checkout;
	}
	public void setCheckout(Date checkout) {
		this.checkout = checkout;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public boolean isStayed() {
		return stayed;
	}
	public void setStayed(boolean stayed) {
		this.stayed = stayed;
	}
	public RoomBean getRoom() {
		return room;
	}
	public void setRoom(RoomBean room) {
		this.room = room;
	}
	public UserBean getUser() {
		return user;
	}
	public void setUser(UserBean user) {
		this.user = user;
	}
	
	public BookingBean(Booking booking) {
		super();
		this.bid = booking.getBid();
		this.bno = booking.getBno();
		this.checkin = booking.getCheckin();
		this.checkout = booking.getCheckout();
		this.days = booking.getDays();
		this.amount = booking.getAmount();
		this.createDate = booking.getCreateDate();
		this.stayed = booking.isStayed();
		this.room = new RoomBean(booking.getRoom());
		this.user = new UserBean(booking.getUser());
	}
	
}
