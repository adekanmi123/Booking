package com.xwkj.booking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xwkj.booking.bean.CommentBean;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Comment;
import com.xwkj.booking.service.CommentManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;

public class CommentManagerImpl extends ManagerTemplate implements CommentManager {

	@Override
	public Map<String, Object> writeComment(String bid, int stars, String content) {
		Map<String, Object> data=new HashMap<>();
		Booking booking=bookingDao.get(bid);
		if(commentDao.findByBooking(booking)!=null) {
			data.put("commented", true);
			return data;
		}
		data.put("commented", false);
		Comment comment=new Comment();
		comment.setStars(stars);
		comment.setContent(content);
		comment.setCommentDate(new Date());
		comment.setEnable(false);
		comment.setBooking(booking);
		String cid=commentDao.save(comment);
		if(cid!=null) {
			data.put("success", true);
			data.put("cid", cid);
		} else {
			data.put("success", false);
		}
		return data;
	}

	@Override
	public CommentBean getComment(String cid) {
		Comment comment=commentDao.get(cid);
		if(comment!=null)
			return new CommentBean(comment);
		return null;
	}

	@Override
	public void deleteComment(String cid) {
		Comment comment=commentDao.get(cid);
		if(comment!=null)
			commentDao.delete(comment);
	}

	@Override
	public int getCommentsCount(String start, String end, String rname, int enable) {
		boolean _enable=false, showAll=false;
		if(enable==-1)
			showAll=true;
		else
			_enable= (enable==1)? true: false;
		Date startDate=null, endDate=null;
		if(!start.equals("")) 
			startDate=DateTool.transferDate(start+" 00:00:00", DateTool.DATE_HOUR_MINUTE_FORMAT);
		if(!end.equals("")) 
			endDate=DateTool.transferDate(end+" 23:59:59", DateTool.DATE_HOUR_MINUTE_FORMAT);
		return commentDao.getCommentsCount(startDate, endDate, rname, showAll, _enable);
	}

	@Override
	public List<CommentBean> searchComments(String start, String end, String rname, int enable, int page, int pageSize) {
		boolean _enable=false, showAll=false;
		if(enable==-1)
			showAll=true;
		else
			_enable= (enable==1)? true: false;
		List<CommentBean> comments=new ArrayList<>();
		int offset=(page-1)*pageSize;
		Date startDate=null, endDate=null;
		if(!start.equals("")) 
			startDate=DateTool.transferDate(start+" 00:00:00", DateTool.DATE_HOUR_MINUTE_FORMAT);
		if(!end.equals("")) 
			endDate=DateTool.transferDate(end+" 23:59:59", DateTool.DATE_HOUR_MINUTE_FORMAT);
		for(Comment comment: commentDao.findByPage(startDate, endDate, rname, showAll, _enable, offset, pageSize))
			comments.add(new CommentBean(comment));
		return comments;
	}

	@Override
	public void checkComment(String cid, boolean enable) {
		Comment comment=commentDao.get(cid);
		comment.setEnable(enable);
		commentDao.update(comment);
	}

	@Override
	public List<CommentBean> getNewestComments(int limit) {
		List<CommentBean> comments=new ArrayList<>();
		for(Comment comment: commentDao.findEnableCommentLimit(limit))
			comments.add(new CommentBean(comment));
		return comments;
	}

}
