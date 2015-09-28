package com.xwkj.booking.service.util;

import com.xwkj.booking.dao.RoomDao;

public class ManagerTemplate {

	protected RoomDao roomDao;

	public RoomDao getRoomDao() {
		return roomDao;
	}

	public void setRoomDao(RoomDao roomDao) {
		this.roomDao = roomDao;
	}
	
	
}
