package com.xwkj.booking.service;

import java.util.List;
import java.util.Map;

import com.xwkj.booking.bean.CommentBean;

public interface CommentManager {

	/**
	 * 撰写评论
	 * @param bid
	 * @param stars
	 * @param content
	 */
	Map<String, Object> writeComment(String bid, int stars, String content);
	
	/**
	 * 获取评论
	 * @param cid
	 * @return
	 */
	CommentBean getComment(String cid);
	
	/**
	 * 删除评论
	 * @param cid
	 */
	void deleteComment(String cid);
	
	/**
	 * 获取评论数量
	 * @param start
	 * @param end
	 * @param rname
	 * @param enable
	 * @return
	 */
	int getCommentsCount(String start, String end, String rname, int enable);
	
	/**
	 * 搜索评论
	 * @param start
	 * @param end
	 * @param rname
	 * @param enable
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<CommentBean> searchComments(String start, String end, String rname, int enable, int page, int pageSize);
	
	/**
	 * 检查评论
	 * @param cid
	 * @param enable
	 */
	void checkComment(String cid, boolean enable);
	
	/**
	 * 获取最新的limit个通过审核的用户评论
	 * @param limit
	 * @return
	 */
	List<CommentBean> getNewestComments(int limit);

	/**
	 * 根据订单id获取评论
	 * @param bid
	 * @return
	 */
	CommentBean getCommentByBid(String bid);
	
	/**
	 * 通过房间id获取评论
	 * @param rid
	 * @return
	 */
	List<CommentBean> getCommentsByRid(String rid);
}
