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
	List<RoomBean> searchRoom(String rname, int number, String location, double minArea, double maxArea, double minPrice, double maxPrice, String start, String end);

}
