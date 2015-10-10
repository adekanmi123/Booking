package com.xwkj.booking.service;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.bean.MessageBean;

public interface MessageManager {

	/**
	 * 新增留言
	 * @param name
	 * @param email
	 * @param telephone
	 * @param content
	 */
	String addMessage(String name, String email, String telephone, String content);
	
	/**
	 * 获得留言
	 * @param mid
	 * @return
	 */
	MessageBean getMessage(String mid);
	
	/**
	 * 获取留言数量
	 * @param start 
	 * @param end
	 * @param name
	 * @param email
	 * @param telephone
	 * @param looked
	 * @return
	 */
	int getMessagesCount(Date start, Date end, String name, String email, String telephone, int looked);
	
	/**
	 * 分页搜索留言
	 * @param start
	 * @param end
	 * @param name
	 * @param email
	 * @param telephone
	 * @param looked
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<MessageBean> searchMessages(Date start, Date end, String name, String email, String telephone, int looked, int page, int pageSize);
}
