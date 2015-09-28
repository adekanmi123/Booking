package com.xwkj.booking.service.impl;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.bean.RoomBean;
import com.xwkj.booking.domain.Room;
import com.xwkj.booking.service.RoomManager;
import com.xwkj.booking.service.util.ManagerTemplate;

public class RoomManagerImpl extends ManagerTemplate implements RoomManager {

	@Override
	public String addRoom(String rname, int number, String location, double area, double price, String descriptor) {
		Room room=new Room();
		room.setRname(rname);
		room.setNumber(number);
		room.setLocation(location);
		room.setArea(area);
		room.setPrice(price);
		room.setDescriptor(descriptor);
		room.setCreateDate(new Date());
		return roomDao.save(room);
	}

	@Override
	public void modifyRoom(String rid, String rname, int number, String location, double area, double price,
			String descriptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RoomBean> searchRoom(String rname, int number, String location, double minArea, double maxArea,
			double minPrice, double maxPrice, Date start, Date end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoomBean> searchRoomAdmin(String rname, int number, String location, Date start, Date end) {
		// TODO Auto-generated method stub
		return null;
	}

}
