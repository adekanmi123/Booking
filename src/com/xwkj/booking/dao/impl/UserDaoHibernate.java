package com.xwkj.booking.dao.impl;

import java.util.List;

import com.xwkj.booking.dao.UserDao;
import com.xwkj.booking.domain.User;
import com.xwkj.common.hibernate3.support.PageHibernateDaoSupport;

public class UserDaoHibernate extends PageHibernateDaoSupport implements UserDao {

	@Override
	public User get(String uid) {
		return getHibernateTemplate().get(User.class, uid);
	}

	@Override
	public String save(User user) {
		return (String)getHibernateTemplate().save(user);
	}

	@Override
	public void update(User user) {
		getHibernateTemplate().update(user);
	}

	@Override
	public void delete(User user) {
		getHibernateTemplate().delete(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public User findUserByTelephone(String telephone) {
		List<User> users=getHibernateTemplate().find("from User where telephone=?", telephone);
		if(users.size()==0)
			return null;
		return users.get(0);
	}

}
