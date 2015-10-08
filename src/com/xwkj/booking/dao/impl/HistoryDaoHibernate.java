package com.xwkj.booking.dao.impl;

import java.sql.SQLException;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.xwkj.booking.dao.HistoryDao;
import com.xwkj.booking.domain.History;
import com.xwkj.booking.domain.Room;
import com.xwkj.common.hibernate3.support.PageHibernateDaoSupport;

public class HistoryDaoHibernate extends PageHibernateDaoSupport implements HistoryDao {

	@Override
	public History get(String hid) {
		return getHibernateTemplate().get(History.class, hid);
	}

	@Override
	public String save(History history) {
		return (String)getHibernateTemplate().save(history);
	}

	@Override
	public void update(History history) {
		getHibernateTemplate().update(history);
	}

	@Override
	public void delete(History history) {
		getHibernateTemplate().delete(history);
	}

	@Override
	public int getReservedCount(Date date, Room room) {
		String hql="select count(*) from History where date=? and room=?";
		int count=getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				query.setParameter(0, date);
				query.setParameter(1, room);
				return (long)query.uniqueResult();
			}
		}).intValue();
		return count;
	}

}
