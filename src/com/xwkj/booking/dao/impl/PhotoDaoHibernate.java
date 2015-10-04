package com.xwkj.booking.dao.impl;

import java.util.List;

import com.xwkj.booking.dao.PhotoDao;
import com.xwkj.booking.domain.Photo;
import com.xwkj.booking.domain.Room;
import com.xwkj.common.hibernate3.support.PageHibernateDaoSupport;

public class PhotoDaoHibernate extends PageHibernateDaoSupport implements PhotoDao {

	@Override
	public Photo get(String pid) {
		return getHibernateTemplate().get(Photo.class, pid);
	}

	@Override
	public String save(Photo photo) {
		return (String)getHibernateTemplate().save(photo);
	}

	@Override
	public void update(Photo photo) {
		getHibernateTemplate().update(photo);
	}

	@Override
	public void delete(Photo photo) {
		getHibernateTemplate().delete(photo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Photo> findByRoom(Room room) {
		return getHibernateTemplate().find("from Photo where room=? order by upload", room);
	}

}
