package com.xwkj.booking.bean;

import java.util.Date;

import com.xwkj.booking.domain.Comment;

public class CommentBean {
	
	private int cid;
	private Integer stars;
	private String content;
	private Date commentDate;
	private Boolean enable;
	private BookingBean booking;
	
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public BookingBean getBooking() {
		return booking;
	}

	public void setBooking(BookingBean booking) {
		this.booking = booking;
	}

	public CommentBean(Comment comment) {
		super();
		this.cid = comment.getCid();
		this.stars = comment.getStars();
		this.content = comment.getContent();
		this.commentDate = comment.getCommentDate();
		this.enable = comment.getEnable();
		this.booking = new BookingBean(comment.getBooking());
	}
}
