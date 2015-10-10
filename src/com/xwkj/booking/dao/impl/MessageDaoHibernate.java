package com.xwkj.booking.dao.impl;

import com.xwkj.booking.dao.MessageDao;
import com.xwkj.booking.domain.Message;
import com.xwkj.common.hibernate3.support.PageHibernateDaoSupport;

public class MessageDaoHibernate extends PageHibernateDaoSupport implements MessageDao {

	@Override
	public Message get(String mid) {
		return getHibernateTemplate().get(Message.class, mid);
	}

	@Override
	public String save(Message message) {
		return (String)getHibernateTemplate().save(message);
	}

	@Override
	public void update(Message message) {
		getHibernateTemplate().update(message);
	}

	@Override
	public void delete(Message message) {
		getHibernateTemplate().delete(message);
	}

}
