package com.xwkj.booking.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.xwkj.booking.bean.UserBean;

public interface UserManager {
	
	public static final String VERIFICATION_CODE="40288bfe5032fea60150331da9de0005";
	public static final String USER_FLAG="8aa501824fb5fb84hdfb607fe100000";
	
	/**
	 * 新增用户
	 * @param telephone
	 * @param uname
	 * @param password
	 * @param tid
	 * @return
	 */
	String addUser(String telephone, String uname, String password, String tid);
	
	/**
	 * 删除用户
	 * @param uid
	 * @return
	 */
	boolean deleteUser(String uid);
	
	/**
	 * 通过uid获得用户
	 * @param uid
	 * @return
	 */
	UserBean getUser(String uid);
	
	/**
	 * 处理用户登录
	 * @param uname
	 * @param password
	 * @param session
	 * @return
	 */
	boolean login(String uname, String password, HttpSession session);
	
	/**
	 * 检查用户session
	 * @param session
	 * @return
	 */
	UserBean checkSession(HttpSession session);

	/**
	 * 修改密码
	 * @param uid
	 * @param password
	 */
	boolean modifyPassword(String uid, String password);
	
	/**
	 * 修改用户名
	 * @param uid
	 * @param uname
	 * @return
	 */
	boolean modifyUname(String uid, String uname);

	/**
	 * 获取短信验证码
	 * @param telephone
	 * @param code
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	Map<String, Object> getVerificationCode(String telephone, String code, HttpSession session)
			throws UnsupportedEncodingException;
	
	/**
	 * 用户注册
	 * @param telephone
	 * @param uname
	 * @param password
	 * @param verificationCode 短信验证码
	 * @param session
	 * @return
	 */
	Map<String, Object> register(String telephone, String uname, String password, String verificationCode, HttpSession session);
}
