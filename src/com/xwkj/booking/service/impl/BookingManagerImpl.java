package com.xwkj.booking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xwkj.booking.bean.BookingBean;
import com.xwkj.booking.bean.UserBean;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Comment;
import com.xwkj.booking.domain.History;
import com.xwkj.booking.domain.Pay;
import com.xwkj.booking.domain.Room;
import com.xwkj.booking.domain.User;
import com.xwkj.booking.service.BookingManager;
import com.xwkj.booking.service.UserManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.MathTool;
import com.xwkj.common.util.SMSService;

import net.sf.json.JSONObject;

public class BookingManagerImpl extends ManagerTemplate implements BookingManager {
	
	private String ReserveFailedDateError;
	private String ReserveFailedNotLogin;
	private String ReserveFailedRoomUnavailable;
	private String ReserveFailedRoomNotEnable;
	
	//支付超时时长
	private int payTimeOut;
	
	//订房成功发送短信模板id
	private int BookingSuccessSMSTemplateID;

	public void setReserveFailedDateError(String reserveFailedDateError) {
		ReserveFailedDateError = reserveFailedDateError;
	}

	public void setReserveFailedNotLogin(String reserveFailedNotLogin) {
		ReserveFailedNotLogin = reserveFailedNotLogin;
	}

	public void setReserveFailedRoomUnavailable(String reserveFailedRoomUnavailable) {
		ReserveFailedRoomUnavailable = reserveFailedRoomUnavailable;
	}

	public void setReserveFailedRoomNotEnable(String reserveFailedRoomNotEnable) {
		ReserveFailedRoomNotEnable = reserveFailedRoomNotEnable;
	}

	public void setBookingSuccessSMSTemplateID(int bookingSuccessSMSTemplateID) {
		BookingSuccessSMSTemplateID = bookingSuccessSMSTemplateID;
	}
	
	public void setPayTimeOut(int payTimeOut) {
		this.payTimeOut = payTimeOut;
	}
	
	public int getPayTimeOut() {
		return payTimeOut;
	}

	@Override
	public Map<String, Object> reserve(String checkin, String checkout, String rid, HttpSession session) {
		Map<String, Object> data=new HashMap<>();
		Room room=roomDao.get(rid);
		//检查房间是否可用
		if(!room.isEnable()) {
			data.put("success", false);
			data.put("reason", ReserveFailedRoomNotEnable);
			return data;
		}
		Date checkinDate=DateTool.transferDate(checkin, DateTool.YEAR_MONTH_DATE_FORMAT);
		Date checkoutDate=DateTool.transferDate(checkout, DateTool.YEAR_MONTH_DATE_FORMAT);
		int days=DateTool.daysBetween(checkinDate, checkoutDate);
		if(days<=0) {
			data.put("success", false);
			data.put("reason", ReserveFailedDateError);
			return data;
		}
		if(session.getAttribute(UserManager.USER_FLAG)==null) {
			data.put("success", false);
			data.put("reason", ReserveFailedNotLogin);
			return data;
		}
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
		booking.setBno(DateTool.formatDate(new Date(), "yyyyMMddHHmmss")+MathTool.getRandomStr(6));
		booking.setCheckin(checkinDate);
		booking.setCheckout(checkoutDate);
		booking.setDays(days);
		booking.setAmount(days*room.getPrice());
		booking.setPay(false);
		booking.setTimeout(false);
		booking.setCreateDate(new Date());
		booking.setStayed(false);
		booking.setRoom(room);
		booking.setUser(user);
		String bid=bookingDao.save(booking);
		data.put("bid", bid);
		data.put("bno", booking.getBno());
		//创建history
		for(Date date=checkinDate; DateTool.daysBetween(date, checkoutDate)>0; date=DateTool.nextDay(date)) {
			History history=new History();
			history.setDate(date);
			history.setBooking(booking);
			history.setRoom(room);
			historyDao.save(history);
		}
		//发送通知短信
		SMSService sms=(SMSService)WebApplicationContextUtils.getWebApplicationContext(WebContextFactory.get().getServletContext()).getBean("SMSService");
		String value="#name#="+user.getUname()+"&#rname#="+booking.getRoom().getRname()+"&#bno#="+booking.getBno()
			+"&#location#="+booking.getRoom().getLocation()+"&#checkin#="+DateTool.formatDate(booking.getCheckin(), DateTool.YEAR_MONTH_DATE_FORMAT_CN)
			+"&#hour#="+(payTimeOut/60);
		JSONObject result=sms.send(user.getTelephone(), BookingSuccessSMSTemplateID, value);
		if(Integer.parseInt(result.get("error_code").toString())==0) {
			data.put("sms", true);
		} else {
			data.put("sms", false);
			System.out.println(result.get("reason"));
		}
		return data;
	}
	
	@Override
	public int getBookingsCountForAdmin(String start, String end, String checkin, String bno, int type) {
		Date startDate=null;
		if(start!=null&&!start.equals(""))
			startDate=DateTool.transferDate(start+" 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT);
		Date endDate=null;
		if(end!=null&&!end.equals(""))
			endDate=DateTool.transferDate(end+" 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT);
		Date checkinDate=null;
		if(checkin!=null&&!checkin.equals(""))
			checkinDate=DateTool.transferDate(checkin, DateTool.YEAR_MONTH_DATE_FORMAT);
		boolean pay=false, timeout=false, showAll=false;
		if(type==0) {
			pay=false;
			timeout=true;
		} else if( type==1) {
			pay=false;
			timeout=false;
		} else if(type==2) {
			pay=true;
			timeout=false;
		} else if( type==-1) {
			showAll=true;
		}
		return bookingDao.getBookingsCountForAdmin(startDate, endDate, checkinDate, bno, pay, timeout, showAll);
	}

	@Override
	public List<BookingBean> searchBookingsForAdmin(String start, String end, String checkin, String bno, int type, int page, int pageSize) {
		List<BookingBean> bookings=new ArrayList<>();
		Date startDate=null;
		if(start!=null&&!start.equals(""))
			startDate=DateTool.transferDate(start+" 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT);
		Date endDate=null;
		if(end!=null&&!end.equals(""))
			endDate=DateTool.transferDate(end+" 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT);
		Date checkinDate=null;
		if(checkin!=null&&!checkin.equals(""))
			checkinDate=DateTool.transferDate(checkin, DateTool.YEAR_MONTH_DATE_FORMAT);
		boolean pay=false, timeout=false, showAll=false;
		if(type==0) {
			pay=false;
			timeout=true;
		} else if( type==1) {
			pay=false;
			timeout=false;
		} else if(type==2) {
			pay=true;
			timeout=false;
		} else if( type==-1) {
			showAll=true;
		}
		int offset=(page-1)*pageSize;
		for(Booking booking: bookingDao.findForAdmin(startDate, endDate, checkinDate, bno, pay, timeout, showAll, offset, pageSize)) 
			bookings.add(new BookingBean(booking));
		return bookings;
	}

	@Override
	public List<BookingBean> getBookingsByUid(String uid, String orderby, boolean desc) {
		List<BookingBean> bookings=new ArrayList<>();
		User user=userDao.get(uid);
		for(Booking booking: bookingDao.findByUser(user, orderby, desc))
			bookings.add(new BookingBean(booking));
		return bookings;
	}

	@Override
	public BookingBean getBooking(String bid) {
		Booking booking=bookingDao.get(bid);
		if(booking!=null)
			return new BookingBean(booking);
		return null;
	}

	@Override
	public BookingBean getBookingByBno(String bno) {
		Booking booking=bookingDao.findByBno(bno);
		if(booking!=null)
			return new BookingBean(booking);
		return null;
	}

	@Override
	public boolean deleteBooking(String bid) {
		Booking booking=bookingDao.get(bid);
		if(booking.getPay())
			return false;
		//删除它占有的房间
		for(History history: historyDao.findByBooking(booking))
			historyDao.delete(history);
		//删除它对应的支付信息
		Pay pay=payDao.findByBooking(booking);
		if(pay!=null)
			payDao.delete(pay);
		//删除评论
		Comment comment=commentDao.findByBooking(booking);
		if(comment!=null)
			commentDao.delete(comment);
		//删除订单
		bookingDao.delete(booking);
		return true;
	}

}
