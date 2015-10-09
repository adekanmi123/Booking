package com.xwkj.booking.dao;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.domain.Booking;

public interface BookingDao {
	
	Booking get(String bid);
	String save(Booking booking);
	void update(Booking booking);
	void delete(Booking booking);
	
	/**
	 * 管理员查询订单
	 * @param start
	 * @param end
	 * @param bno
	 * @return
	 */
	List<Booking> findForAdmin(Date start, Date end, Date checkin, String bno);
	
}
