package com.xwkj.booking.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.xwkj.booking.bean.BookingBean;

public interface BookingManager {

	/**
	 * 预定房间
	 * @param checkin
	 * @param checkout
	 * @param rid
	 * @param session 用户信息从session中获取，未登录的用户不允许预定房间
	 * @return
	 */
	Map<String, Object> reserve(String checkin, String checkout, String rid, HttpSession session);
	
	/**
	 * 管理员查询订单
	 * @param start
	 * @param end
	 * @param checkin
	 * @param bno
	 * @return
	 */
	List<BookingBean> searchBookingsForAdmin(String start, String end, String checkin, String bno);
	
	/**
	 * 用户获取自己的订单
	 * @param uid 用户id
	 * @param orderby 排序关键字
	 * @param desc 排序顺序
	 * @return
	 */
	List<BookingBean> getBookingsByUid(String uid, String orderby, boolean desc);
	
	/**
	 * 获取订单
	 * @param bid
	 * @return
	 */
	BookingBean getBooking(String bid);
	
}
