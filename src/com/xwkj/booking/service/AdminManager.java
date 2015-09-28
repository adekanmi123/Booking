package com.xwkj.booking.service;

import javax.servlet.http.HttpSession;

public interface AdminManager 
{
	public static final String ADMIN_FLAG="402881ef4fcab170014fcae3d80b001c";
	
	/**
	 * 管理员登陆
	 * @param username
	 * @param password
	 * @return
	 */
	boolean login(String username,String password, HttpSession session);
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	String checkSession(HttpSession session);
}
