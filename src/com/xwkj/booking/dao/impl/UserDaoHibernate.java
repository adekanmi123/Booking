package com.xwkj.booking.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

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

	@Override
	public int getUsersCount(String telephone, String uname) {
		String hql="select count(*) from User where uname like ? and telephone like ?";
		int count=getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				query.setParameter(0, "%"+uname+"%");
				query.setParameter(1, "%"+telephone+"%");
				return (long)query.uniqueResult();
			}
		}).intValue();
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUsersByPage(String telephone, String uname, int offset, int pageSize) {
		String hql="from User where uname like ? and telephone like ? order by registerDate desc";
		return findByPage(hql, new Object [] {
			"%"+uname+"%",
			"%"+telephone+"%"
		}, offset, pageSize);
	}

}
