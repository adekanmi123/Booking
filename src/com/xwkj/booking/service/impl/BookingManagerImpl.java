package com.xwkj.booking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.xwkj.booking.bean.UserBean;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.History;
import com.xwkj.booking.domain.Room;
import com.xwkj.booking.domain.User;
import com.xwkj.booking.service.BookingManager;
import com.xwkj.booking.service.UserManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;

public class BookingManagerImpl extends ManagerTemplate implements BookingManager {
	
	private String ReserveFailedDateError;
	private String ReserveFailedNotLogin;
	private String ReserveFailedRoomUnavailable;

	public void setReserveFailedDateError(String reserveFailedDateError) {
		ReserveFailedDateError = reserveFailedDateError;
	}

	public void setReserveFailedNotLogin(String reserveFailedNotLogin) {
		ReserveFailedNotLogin = reserveFailedNotLogin;
	}

	public void setReserveFailedRoomUnavailable(String reserveFailedRoomUnavailable) {
		ReserveFailedRoomUnavailable = reserveFailedRoomUnavailable;
	}


	@Override
	public Map<String, Object> reserve(String checkin, String checkout, String rid, HttpSession session) {
		Map<String, Object> data=new HashMap<>();
		Date checkinDate=DateTool.transferDate(checkin, DateTool.YEAR_MONTH_DATE_FORMAT);
		Date checkoutDate=DateTool.transferDate(checkout, DateTool.YEAR_MONTH_DATE_FORMAT);
		int days=DateTool.daysBetween(checkinDate, checkoutDate);
		if(days<=0) {
			data.put("success", false);
			data.put("reason", ReserveFailedDateError);
			return data;
		}
		if(session.getAttribute(UserManager.USER_FLAG)==null) {
			data.put("reason", false);
			data.put("reason", ReserveFailedNotLogin);
			return data;
		}
		Room room=roomDao.get(rid);
		List<Date> unavailableDates=new ArrayList<>();
		//检查空房情况
		for(Date date=checkinDate; DateTool.daysBetween(date, checkoutDate)>0; date=DateTool.nextDay(date)) {
			if(historyDao.getReservedCount(date, room)==room.getAvailable())
				unavailableDates.add(date);
		}
		if(unavailableDates.size()>0) {
			data.put("success", false);
			data.put("reason", ReserveFailedRoomUnavailable);
			data.put("unavailableDates", unavailableDates);
			return data;
		}
		data.put("success", true);
		String uid=((UserBean)session.getAttribute(UserManager.USER_FLAG)).getUid();
		User user=userDao.get(uid);
		
		Booking booking=new Booking();
		booking.setCheckin(checkinDate);
		booking.setCheckout(checkoutDate);
		booking.setDays(days);
		booking.setAmount(days*room.getPrice());
		booking.setCreateDate(new Date());
		booking.setPayed(false);
		booking.setStayed(false);
		booking.setRoom(room);
		booking.setUser(user);
		String bid=bookingDao.save(booking);
		data.put("bid", bid);
		//创建history
		for(Date date=checkinDate; DateTool.daysBetween(date, checkoutDate)>0; date=DateTool.nextDay(date)) {
			History history=new History();
			history.setDate(date);
			history.setBooking(booking);
			history.setRoom(room);
			historyDao.save(history);
		}
		//房间的销售数量加1
		room.setSold(room.getSold()+1);
		roomDao.update(room);
		return data;
	}


}
