package com.xwkj.booking.service.impl;

import java.util.Date;

import com.xwkj.booking.bean.PayBean;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Pay;
import com.xwkj.booking.service.PayManager;
import com.xwkj.booking.service.util.ManagerTemplate;

public class PayManagerImpl extends ManagerTemplate implements PayManager {
	
	private int payedSMSTemplateID;

	public int getPayedSMSTemplateID() {
		return payedSMSTemplateID;
	}

	public void setPayedSMSTemplateID(int payedSMSTemplateID) {
		this.payedSMSTemplateID = payedSMSTemplateID;
	}
	
	@Override
	public PayBean getPayByBno(String bno) {
		Booking booking=bookingDao.findByBno(bno);
		Pay pay=payDao.findByBooking(booking);
		//如果没有支付信息、就创建支付信息pay
		if(pay==null) {
			pay=new Pay();
			pay.setPayed(false);
			pay.setBooking(booking);
			payDao.save(pay);
		}
		return new PayBean(pay);
	}

	@Override
	public void writeSign(String bno, String sign) {
		Booking booking=bookingDao.findByBno(bno);
		Pay pay=payDao.findByBooking(booking);
		pay.setSign(sign);
		payDao.update(pay);
	}

	@Override
	public void updatePayedState(String bno) {
		Booking booking=bookingDao.findByBno(bno);
		Pay pay=payDao.findByBooking(booking);
		pay.setPayed(true);
		pay.setPayDate(new Date());
		payDao.update(pay);
	}

}
