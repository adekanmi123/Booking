package com.xwkj.booking.dao.impl;

import com.xwkj.booking.dao.BookingDao;
import com.xwkj.booking.domain.Booking;
import com.xwkj.common.hibernate3.support.PageHibernateDaoSupport;

public class BookingDaoHibernate extends PageHibernateDaoSupport implements BookingDao {

	@Override
	public Booking get(String bid) {
		return getHibernateTemplate().get(Booking.class, bid);
	}

	@Override
	public String save(Booking booking) {
		return (String)getHibernateTemplate().save(booking);
	}

	@Override
	public void update(Booking booking) {
		getHibernateTemplate().update(booking);
	}

	@Override
	public void delete(Booking booking) {
		getHibernateTemplate().delete(booking);
	}

}
