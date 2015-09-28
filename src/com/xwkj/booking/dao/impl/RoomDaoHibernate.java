package com.xwkj.booking.dao.impl;

import com.xwkj.booking.dao.RoomDao;
import com.xwkj.booking.domain.Room;
import com.xwkj.common.util.PageHibernateDaoSupport;

public class RoomDaoHibernate extends PageHibernateDaoSupport implements RoomDao {

	@Override
	public Room get(String rid) {
		return getHibernateTemplate().get(Room.class, rid);
	}

	@Override
	public String save(Room room) {
		return (String)getHibernateTemplate().save(room);
	}

	@Override
	public void update(Room room) {
		getHibernateTemplate().update(room);
	}

	@Override
	public void delete(Room room) {
		getHibernateTemplate().delete(room);
	}

}
