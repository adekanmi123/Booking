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
	public String addRoom(String rname, int number, String location, double area, double price, String descriptor) {
		Room room=new Room();
		room.setRname(rname);
		room.setNumber(number);
		room.setLocation(location);
		room.setArea(area);
		room.setPrice(price);
		room.setDescriptor(descriptor);
		room.setCreateDate(new Date());
		room.setSold(0);
		return roomDao.save(room);
	}

	@Override
	public void modifyRoom(String rid, String rname, int number, String location, double area, double price, String descriptor) {
		Room room=roomDao.get(rid);
		room.setRname(rname);
		room.setNumber(number);
		room.setLocation(location);
		room.setArea(area);
		room.setPrice(price);
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
	public List<RoomBean> searchRoom(String rname, int number, String location, double minArea, double maxArea,
			double minPrice, double maxPrice, String start, String end) {
		Date startDate=DateTool.transferDate(start+" 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT);
		Date endDate=DateTool.transferDate(end+" 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT);
		List<RoomBean> rooms=new ArrayList<>();
		for(Room room: roomDao.searchRoom(rname, number, location, minArea, maxArea, minPrice, maxPrice, startDate, endDate))
			rooms.add(new RoomBean(room));
		return rooms;
	}

}
