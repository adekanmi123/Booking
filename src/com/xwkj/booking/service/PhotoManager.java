package com.xwkj.booking.service;

import java.util.List;

import com.xwkj.booking.bean.PhotoBean;

public interface PhotoManager {
	
	/**
	 * 获取房间的所有照片
	 * @param rid
	 * @return
	 */
	List<PhotoBean> getPhotosByRid(String rid);
	
	/**
	 * 删除照片
	 * @param pid
	 */
	void deletePhoto(String pid);
	
	/**
	 * 设置图片为其对应的房间封面
	 * @param pid
	 */
	void setAsRoomCover(String pid);
}
