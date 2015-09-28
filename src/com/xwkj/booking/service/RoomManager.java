package com.xwkj.booking.service;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.bean.RoomBean;

public interface RoomManager {
	
	/**
	 * 新增房间
	 * @param rname
	 * @param number
	 * @param location
	 * @param area
	 * @param price
	 * @param descriptor
	 * @return
	 */
	String addRoom(String rname, int number, String location, double area, double price, String descriptor);
	
	/**
	 * 修改房间
	 * @param rid
	 * @param rname
	 * @param number
	 * @param location
	 * @param area
	 * @param price
	 * @param descriptor
	 */
	void modifyRoom(String rid, String rname, int number, String location, double area, double price, String descriptor);
	
	List<RoomBean> searchRoomAdmin(String rname, int number, String location, Date start, Date end);
	
	/**
	 * 查询房间
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
	List<RoomBean> searchRoom(String rname, int number, String location, double minArea, double maxArea, double minPrice, double maxPrice, Date start, Date end);

}
