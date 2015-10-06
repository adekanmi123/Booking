package com.xwkj.booking.dao;

import com.xwkj.booking.domain.User;

public interface UserDao {
	
	User get(String uid);
	String save(User user);
	void update(User user);
	void delete(User user);
	
	/**
	 * 
	 * @param telephone
	 * @return
	 */
	User findUserByTelephone(String telephone);
}
