package com.xwkj.booking.dao;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.domain.Room;

public interface RoomDao {
	
	Room get(String rid);
	String save(Room room);
	void update(Room room);
	void delete(Room room);

	/**
	 * 搜索房间
	 * @param rname
	 * @param number
	 * @param location
	 * @param minArea
	 * @param maxArea
	 * @param minPrice
	 * @param maxPrice
	 * @param start
	 * @param end
	 * @return
	 */
	List<Room> searchRoom(String rname, Integer number, String location, Double minArea, Double maxArea,
			Double minPrice, Double maxPrice, Date start, Date end);
}
