package com.xwkj.booking.service;

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
	
	/**
	 * 删除房间
	 * @param rid
	 * @return
	 */
	boolean removeRoom(String rid);
	
	/**
	 * 获取房间
	 * @param rid
	 * @return
	 */
	RoomBean getRoom(String rid);
	
	/**
	 * @param limit
	 * @return
	 */
	List<RoomBean> getNewestRooms(int limit);
	
	/**
	 * 管理员查询房间
	 * @param start
	 * @param end
	 * @param number
	 * @param location
	 * @param rname
	 * @return
	 */
	List<RoomBean> searchRoomForAdmin(String start, String end, int number, String location, String rname);
	
	/**
	 * 用户查询房间
	 * @param checkin
	 * @param checkout
	 * @param number
	 * @param location
	 * @param rname
	 * @param minPrice
	 * @param maxPrice
	 * @return
	 */
	List<RoomBean> searchRoomForUser(String checkin, String checkout, int number, String location, String rname, double minPrice, double maxPrice);

}
