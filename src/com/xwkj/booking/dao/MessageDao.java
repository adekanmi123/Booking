package com.xwkj.booking.dao;

import com.xwkj.booking.domain.Message;

public interface MessageDao {
	
	Message get(String mid);
	String save(Message message);
	void update(Message message);
	void delete(Message message);
	
}
