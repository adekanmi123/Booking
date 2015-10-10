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
	String addRoom(String rname, int number, String location, double area, double price, int available, String descriptor);
	
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
	void modifyRoom(String rid, String rname, int number, String location, double area, double price, int available, String descriptor);
	
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
	 * 获取最新的limit个房间
	 * @param limit
	 * @return
	 */
	List<RoomBean> getNewestRooms(int limit);
	
	
	/**
	 * 管理员获取房间个数
	 * @param location
	 * @param rname
	 * @param number
	 * @param enable
	 * @return
	 */
	int getRoomCountForAdmin(String location, String rname, int number, int enable);
	
	/**
	 * 管理员查询房间
	 * @param location 房间位置
	 * @param rname 房间名称
	 * @param number 房间类型
	 * @param enable 房间是否可用 可用1 不可用0 任意-1
	 * @param page 页码
	 * @param pageSize 页面长度
	 * @return
	 */
	List<RoomBean> searchRoomForAdmin(String location, String rname, int number, int enable, int page, int pageSize);
	
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
	
	/**
	 * 更改房间可用状态
	 * @param rid
	 * @param enable
	 */
	void enableRoom(String rid, boolean enable);

}
