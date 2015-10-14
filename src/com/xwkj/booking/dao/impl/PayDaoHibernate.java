package com.xwkj.booking.dao.impl;

import java.util.List;

import com.xwkj.booking.dao.PayDao;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Pay;
import com.xwkj.common.hibernate3.support.PageHibernateDaoSupport;

public class PayDaoHibernate extends PageHibernateDaoSupport implements PayDao {

	@Override
	public Pay get(String pid) {
		return getHibernateTemplate().get(Pay.class, pid);
	}

	@Override
	public String save(Pay pay) {
		return (String)getHibernateTemplate().save(pay);
	}

	@Override
	public void update(Pay pay) {
		getHibernateTemplate().update(pay);
	}

	@Override
	public void delete(Pay pay) {
		getHibernateTemplate().delete(pay);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pay findByBooking(Booking booking) {
		List<Pay> pays=getHibernateTemplate().find("from Pay where booking=?", booking);
		if(pays.size()>0)
			return pays.get(0);
		return null;
	}

}
