package com.xwkj.booking.service;

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
	int getMessagesCount(String start, String end, String name, String email, String telephone, int looked);
	
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
	List<MessageBean> searchMessages(String start, String end, String name, String email, String telephone, int looked, int page, int pageSize);
	
	/**
	 * 标记已读和未读
	 * @param mid
	 * @param looked
	 */
	void looked(String mid, boolean looked);
	
	/**
	 * 邮件回复
	 * @param mid
	 * @param reply
	 * @return
	 */
	boolean replyByEmail(String mid, String reply);
	
	/**
	 * 短信回复
	 * @param mid
	 * @param reply
	 * @return
	 */
	boolean replyBySMS(String mid, String reply);
	
	/**
	 * 删除留言
	 * @param mid
	 */
	void removeMessage(String mid);
}
