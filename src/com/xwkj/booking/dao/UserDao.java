package com.xwkj.booking.dao;

import java.util.List;

import com.xwkj.booking.domain.User;

public interface UserDao {
	
	User get(String uid);
	String save(User user);
	void update(User user);
	void delete(User user);
	
	/**
	 * 通过电话号码查找用户
	 * @param telephone 电话号码
	 * @return
	 */
	User findUserByTelephone(String telephone);
	
	/**
	 * 查询用户数量
	 * @param uname 用户名
	 * @param telephone 电话号码
	 * @return
	 */
	int getUsersCount(String telephone, String uname);
	
	/**
	 * 分页查询用户
	 * @param uname 用户名
	 * @param telephone 电话号码
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	List<User> findUsersByPage(String telephone, String uname, int offset, int pageSize);
}
