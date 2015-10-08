package com.xwkj.booking.dao;

import com.xwkj.booking.domain.Booking;

public interface BookingDao {
	
	Booking get(String bid);
	String save(Booking booking);
	void update(Booking booking);
	void delete(Booking booking);
	
}
