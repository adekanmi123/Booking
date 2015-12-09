package com.xwkj.booking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.directwebremoting.WebContextFactory;

import com.xwkj.booking.bean.RoomBean;
import com.xwkj.booking.domain.Photo;
import com.xwkj.booking.domain.Room;
import com.xwkj.booking.service.RoomManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.booking.servlet.PhotoServlet;
import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.FileTool;

public class RoomManagerImpl extends ManagerTemplate implements RoomManager {

	@Override
	public String addRoom(String rname, int number, String location, String transportation, double latitude, double longitude, int level,
			double area, double price, int available, String descriptor) {
		Room room=new Room();
		room.setRname(rname);
		room.setNumber(number);
		room.setLocation(location);
		room.setTransportation(transportation);
		room.setLatitude(latitude);
		room.setLongitude(longitude);
		room.setLevel(level);
		room.setArea(area);
		room.setPrice(price);
		room.setDescriptor(descriptor);
		room.setCreateDate(new Date());
		room.setSold(0);
		room.setAvailable(available);
		room.setEnable(true);
		return roomDao.save(room);
	}

	@Override
	public void modifyRoom(String rid, String rname, int number, String location, String transportation, double latitude, double longitude, 
			int level, double area, double price, int available, String descriptor) {
		Room room=roomDao.get(rid);
		room.setRname(rname);
		room.setNumber(number);
		room.setLocation(location);
		room.setTransportation(transportation);
		room.setLatitude(latitude);
		room.setLongitude(longitude);
		room.setLevel(level);
		room.setArea(area);
		room.setPrice(price);
		room.setAvailable(available);
		room.setDescriptor(descriptor);
		roomDao.update(room);
	}

	@Override
	public boolean removeRoom(String rid) {
		Room room=roomDao.get(rid);
		//如果房间已被售出，就不让被删除
		if(room.getSold()>0)
			return false;
		//封面置空
		room.setCover(null);
		roomDao.update(room);
		//删除照片
		for(Photo photo: photoDao.findByRoom(room))
			photoDao.delete(photo);
		String rootPath=WebContextFactory.get().getServletContext().getRealPath("/");
		String path=rootPath+PhotoServlet.PHOTO_FOLDER+"/"+rid;
		FileTool.delAllFile(path);
		FileTool.delFolder(path);
		//删除房间
		roomDao.delete(room);
		return true;
	}

	@Override
	public RoomBean getRoom(String rid) {
		Room room=roomDao.get(rid);
		if(room!=null)
			return new RoomBean(room);
		return null;
	}
	
	@Override
	public List<RoomBean> getNewestRooms(int limit) {
		List<RoomBean> rooms=new ArrayList<>();
		for(Room room: roomDao.finidRoomLimit(limit))
			rooms.add(new RoomBean(room));
		return rooms;
	}
	
	@Override
	public int getRoomCountForAdmin(String location, String rname, int number, int enable) {
		boolean _enable=false, showAll=false;
		if(enable==-1)
			showAll=true;
		else
			_enable= (enable==1)? true: false;
		return roomDao.getRoomCount(location, rname, number, showAll, _enable);
	}
	
	@Override
	public List<RoomBean> searchRoomForAdmin(String location, String rname, int number, int enable, int page, int pageSize) {
		List<RoomBean> rooms=new ArrayList<>();
		int offset=(page-1)*pageSize;
		boolean _enable=false, showAll=false;
		if(enable==-1)
			showAll=true;
		else
			_enable= (enable==1)? true: false;
		for(Room room: roomDao.searchByPage(location, rname, number, showAll, _enable, offset, pageSize))
			rooms.add(new RoomBean(room));
		return rooms;
	}

	@Override
	public List<RoomBean> searchRoomForUser(String checkin, String checkout, int number, String location, String rname, double minPrice, double maxPrice) {
		Date checkinDate=DateTool.transferDate(checkin, DateTool.YEAR_MONTH_DATE_FORMAT);
		Date checkoutDate=DateTool.transferDate(checkout, DateTool.YEAR_MONTH_DATE_FORMAT);
		List<RoomBean> rooms=new ArrayList<>();
		for(Room room: roomDao.searchRoom(rname, number, location, -1.0, -1.0, minPrice, maxPrice, null, null, false, true)) 
			if(isRoomAvaliable(checkinDate, checkoutDate, room))
				rooms.add(new RoomBean(room));
		return rooms;
	}

	@Override
	public void enableRoom(String rid, boolean enable) {
		Room room=roomDao.get(rid);
		room.setEnable(enable);
		room.setCreateDate(new Date());
		roomDao.update(room);
	}
	
	/**
	 * //检查该房间在该时间段是否已经满房间
	 * @param checkin 入住时间
	 * @param checkout 退房时间
	 * @param room 房间
	 * @return
	 */
	private boolean isRoomAvaliable(Date checkin, Date checkout, Room room) {
		for(Date date=checkin; DateTool.daysBetween(date, checkout)>0; date=DateTool.nextDay(date)) 
			if(historyDao.getReservedCount(date, room)==room.getAvailable())
				return false;
		return true;
	}

}
