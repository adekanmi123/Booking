package com.xwkj.booking.dao;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.History;
import com.xwkj.booking.domain.Room;

public interface HistoryDao {
	
	History get(String hid);
	String save(History history);
	void update(History history);
	void delete(History history);

	/**
	 * 查询一个房间在指定日期地预订数量
	 * @param date 
	 * @param room
	 * @return
	 */
	int getReservedCount(Date date, Room room);
	
	/**
	 * 查找一个订单对应的订房记录
	 * @param booking
	 * @return
	 */
	List<History> findByBooking(Booking booking);
}
