package com.xwkj.booking.dao;

import com.xwkj.booking.domain.History;

public interface HistoryDao {
	
	History get(String hid);
	String save(History history);
	void update(History history);
	void delete(History history);

}
