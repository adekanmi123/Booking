package com.xwkj.booking.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

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

	@Override
	public int getMessagesCount(Date start, Date end, String name, String email, String telephone, boolean showAll,
			boolean looked) {
		String hql="select count(*) from Message where mid!=null";
		List<Object> objects=new ArrayList<>();
		if(start!=null) {
			hql+=" and date>=?";
			objects.add(start);
		}
		if(end!=null) {
			hql+=" and date<=?";
			objects.add(end);
		}
		if(name!=null&&!name.equals("")) {
			hql+=" and name like ?";
			objects.add("%"+name+"%");
		}
		if(email!=null&&!email.equals("")) {
			hql+=" and email like ?";
			objects.add("%"+email+"%");
		}
		if(telephone!=null&&!telephone.equals("")) {
			hql+=" and telephone like ?";
			objects.add("%"+telephone+"%");
		}
		if(!showAll) {
			hql+=" and looked=?";
			objects.add(looked);
		}
		final String _hql=hql;
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Query query=session.createQuery(_hql);
				for(int i=0; i< objects.size(); i++)
					query.setParameter(i, objects.get(i));
				return (long)query.uniqueResult();
			}
		}).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> findByPage(Date start, Date end, String name, String email, String telephone, boolean showAll,
			boolean looked, int offset, int pageSize) {
		String hql="from Message where mid!=null";
		List<Object> objects=new ArrayList<>();
		if(start!=null) {
			hql+=" and date>=?";
			objects.add(start);
		}
		if(end!=null) {
			hql+=" and date<=?";
			objects.add(end);
		}
		if(name!=null&&!name.equals("")) {
			hql+=" and name like ?";
			objects.add("%"+name+"%");
		}
		if(email!=null&&!email.equals("")) {
			hql+=" and email like ?";
			objects.add("%"+email+"%");
		}
		if(telephone!=null&&!telephone.equals("")) {
			hql+=" and telephone like ?";
			objects.add("%"+telephone+"%");
		}
		if(!showAll) {
			hql+=" and looked=?";
			objects.add(looked);
		}
		hql+=" order by date desc";
		Object [] objs=new Object[objects.size()];
		for(int i=0; i<objects.size(); i++)
			objs[i]=objects.get(i);
		return findByPage(hql, objs, offset, pageSize);
	}

}
