package com.xwkj.booking.dao;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.domain.Message;

public interface MessageDao {
	
	Message get(String mid);
	String save(Message message);
	void update(Message message);
	void delete(Message message);
	
	/**
	 * 获取留言数量
	 * @param start
	 * @param end
	 * @param name
	 * @param email
	 * @param telephone
	 * @param showAll
	 * @param looked
	 * @return
	 */
	int getMessagesCount(Date start, Date end, String name, String email, String telephone, boolean showAll, boolean looked);
	
	/**
	 * 分页查询消息
	 * @param start
	 * @param end
	 * @param name
	 * @param email
	 * @param telephone
	 * @param showAll
	 * @param looked
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	List<Message> findByPage(Date start, Date end, String name, String email, String telephone, boolean showAll, boolean looked, int offset, int pageSize);
}
