package com.xwkj.booking.domain;

import java.io.Serializable;
import java.util.Date;

public class History implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String hid;
	private Date date;
	private Booking booking;
	private Room room;
	
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}

}
