package com.xwkj.booking.dao;

import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Pay;

public interface PayDao {
	Pay get(String pid);
	String save(Pay pay);
	void update(Pay pay);
	void delete(Pay pay);
	
	/**
	 * 通过订单查询支付信息
	 * @param booking
	 * @return
	 */
	Pay findByBooking(Booking booking);
}
