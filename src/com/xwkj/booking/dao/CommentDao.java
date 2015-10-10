package com.xwkj.booking.dao;

import com.xwkj.booking.domain.Comment;

public interface CommentDao {
	
	Comment get(String cid);
	String save(Comment comment);
	void update(Comment comment);
	void delete(Comment comment);
	
}
