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
	 * @param minArea 最小面积
	 * @param maxArea 最大面积
	 * @param minPrice 最低价格
	 * @param maxPrice 最高价格
	 * @param start 
	 * @param end
	 * @param enable
	 * @return
	 */
	List<Room> searchRoom(String rname, Integer number, String location, Double minArea, Double maxArea,
			Double minPrice, Double maxPrice, Date start, Date end, boolean all, boolean enable);
	
	/**
	 * 限制数量查找房间
	 * @param limit
	 * @return
	 */
	List<Room> finidRoomLimit(int limit);
}
