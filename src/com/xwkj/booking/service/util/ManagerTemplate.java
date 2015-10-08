package com.xwkj.booking.service.util;

import com.xwkj.booking.dao.BookingDao;
import com.xwkj.booking.dao.HistoryDao;
import com.xwkj.booking.dao.PhotoDao;
import com.xwkj.booking.dao.RoomDao;
import com.xwkj.booking.dao.UserDao;

public class ManagerTemplate {

	protected RoomDao roomDao;
	protected PhotoDao photoDao;
	protected UserDao userDao;
	protected BookingDao bookingDao;
	protected HistoryDao historyDao;

	public RoomDao getRoomDao() {
		return roomDao;
	}

	public void setRoomDao(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	public PhotoDao getPhotoDao() {
		return photoDao;
	}

	public void setPhotoDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public BookingDao getBookingDao() {
		return bookingDao;
	}

	public void setBookingDao(BookingDao bookingDao) {
		this.bookingDao = bookingDao;
	}

	public HistoryDao getHistoryDao() {
		return historyDao;
	}

	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}
	
}
