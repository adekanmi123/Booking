package com.xwkj.booking.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

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
	
}
