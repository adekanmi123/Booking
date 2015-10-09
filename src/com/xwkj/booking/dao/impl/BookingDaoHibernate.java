package com.xwkj.booking.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Booking> findForAdmin(Date start, Date end, Date checkin, String bno) {
		String hql="from Booking where bno!=null ";
		List<Object> objects=new ArrayList<>();
		if(start!=null) {
			hql+=" and createDate>=?";
			objects.add(start);
		}
		if(end!=null) {
			hql+=" and createDate<=?";
			objects.add(end);
		}
		if(checkin!=null) {
			hql+=" and checkin=?";
			objects.add(checkin);
		}
		if(bno!=null&&!bno.equals("")) {
			hql+="  and bno like ?";
			objects.add("%"+bno+"%");
		}
		hql+=" order by createDate desc";
		Object [] objs=new Object[objects.size()];
		for(int i=0; i<objects.size(); i++)
			objs[i]=objects.get(i);
		return getHibernateTemplate().find(hql, objs);
	}

}
