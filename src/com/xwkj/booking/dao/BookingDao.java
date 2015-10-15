package com.xwkj.booking.dao;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.User;

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
	
	/**
	 * 查询用户的订单
	 * @param user
	 * @param orderby
	 * @param desc
	 * @return
	 */
	List<Booking> findByUser(User user, String orderby, boolean desc);
	
	/**
	 * 通过订单号查询订单
	 * @param bno
	 * @return
	 */
	Booking findByBno(String bno);
	
	/**
	 * 查询超时未支付但还没标记超时的订单
	 * @param time
	 * @return
	 */
	List<Booking> findWillTimoutBookings(Date time);
	
	/**
	 * 查询已入住但还没有标记入住的订单
	 * @param date
	 * @return
	 */
	List<Booking> findWillStayedBookings(Date date);
	
}
