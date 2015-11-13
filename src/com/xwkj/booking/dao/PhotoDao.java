package com.xwkj.booking.dao;

import java.util.List;

import com.xwkj.booking.domain.Photo;
import com.xwkj.booking.domain.Room;

public interface PhotoDao {

	Photo get(String pid);
	String save(Photo photo);
	void update(Photo photo);
	void delete(Photo photo);
	
	/**
	 * @param room
	 * @return
	 */
	List<Photo> findByRoom(Room room);

}
