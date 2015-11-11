package com.xwkj.booking.schedule;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xwkj.booking.dao.BookingDao;
import com.xwkj.booking.dao.HistoryDao;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.History;
import com.xwkj.booking.service.BookingManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;

public class CloseTimeoutBookingsJob extends QuartzJobBean {
	private ManagerTemplate managerTemplate;
	private BookingManager bookingManager;

	public void setBookingManager(BookingManager bookingManager) {
		this.bookingManager = bookingManager;
	}

	public void setManagerTemplate(ManagerTemplate managerTemplate) {
		this.managerTemplate = managerTemplate;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		BookingDao bookingDao=managerTemplate.getBookingDao();
		HistoryDao historyDao=managerTemplate.getHistoryDao();
		Date time=DateTool.nextMinute(new Date(), -bookingManager.getPayTimeOut());
		//揪出那些超时都没有支付的订单
		List<Booking> bookings=bookingDao.findWillTimoutBookings(time);
		for(Booking booking: bookings) {
			System.out.println(booking.getBno()+" created in "+DateTool.formatDate(booking.getCreateDate(), DateTool.DATE_HOUR_MINUTE_FORMAT)+" is timeout.");
			//把他们的超时标志位设问题true
			booking.setTimeout(true);
			bookingDao.update(booking);
			//释放他们所占有的房间
			List<History> histories=historyDao.findByBooking(booking);
			for(History history: histories)
				historyDao.delete(history);
		}
	}
}
