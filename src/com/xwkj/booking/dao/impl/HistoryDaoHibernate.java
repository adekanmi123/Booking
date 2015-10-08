package com.xwkj.booking.dao.impl;

import com.xwkj.booking.dao.HistoryDao;
import com.xwkj.booking.domain.History;
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

}
