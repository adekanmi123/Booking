package com.xwkj.booking.schedule;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xwkj.booking.dao.BookingDao;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.service.util.ManagerTemplate;

/**
 * 更新用户入住信息，已支付的订单在入住之后的第二天订单将标记为已入住
 * @author limeng
 *
 */
public class UpdateStayedJob extends QuartzJobBean {
	private ManagerTemplate managerTemplate;
	
	public void setManagerTemplate(ManagerTemplate managerTemplate) {
		this.managerTemplate = managerTemplate;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		BookingDao bookingDao=managerTemplate.getBookingDao();
		List<Booking> bookings=bookingDao.findWillStayedBookings(new Date());
		for(Booking booking: bookings) {
			booking.setStayed(true);
			bookingDao.update(booking);
		}
	}

}
