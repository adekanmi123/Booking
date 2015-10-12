package com.xwkj.booking.dao;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Comment;
import com.xwkj.booking.domain.Room;

public interface CommentDao {
	
	Comment get(String cid);
	String save(Comment comment);
	void update(Comment comment);
	void delete(Comment comment);
	
	/**
	 * 通过订单查找评论
	 * @param booking
	 * @return
	 */
	Comment findByBooking(Booking booking);
	
	/**
	 * 获取评论数量
	 * @param start
	 * @param end
	 * @param rname
	 * @param showAll
	 * @param enable
	 * @return
	 */
	int getCommentsCount(Date start, Date end, String rname, boolean showAll, boolean enable);
	
	/**
	 * 分页查询评论
	 * @param start
	 * @param end
	 * @param rname
	 * @param showAll
	 * @param enable
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	List<Comment> findByPage(Date start, Date end, String rname, boolean showAll, boolean enable, int offset, int pageSize);
	
	/**
	 * 查询最新的limit个通过审核的评论
	 * @param limit
	 * @return
	 */
	List<Comment> findEnableCommentLimit(int limit);
	
	/**
	 * 查找房间评论
	 * @param room
	 * @return
	 */
	List<Comment> findByRoom(Room room);
}
