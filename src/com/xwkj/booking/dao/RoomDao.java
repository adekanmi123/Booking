package com.xwkj.booking.dao;

import com.xwkj.booking.domain.Room;

public interface RoomDao {
	
	Room get(String rid);
	String save(Room room);
	void update(Room room);
	void delete(Room room);

}
