package com.xwkj.booking.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.xwkj.booking.dao.RoomDao;
import com.xwkj.booking.domain.Room;
import com.xwkj.common.hibernate3.support.PageHibernateDaoSupport;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> searchRoom(String rname, Integer number, String location, Double minArea, Double maxArea,
			Double minPrice, Double maxPrice, Date start, Date end) {
		List<Object> objects=new ArrayList<>();
		String hql="from Room where rid!=null ";
		if(start!=null) {
			hql+=" and createDate>=?";
			objects.add(start);
		}
		if(end!=null) {
			hql+=" and createDate<=?";
			objects.add(end);
		}
		if(rname!=null) {
			hql+=" and rname like ?";
			objects.add("%"+rname+"%");
		}
		if(number>0) {
			hql+=" and number=?";
			objects.add(number);
		}
		if(location!=null) {
			hql+=" and location like ?";
			objects.add("%"+location+"%");
		}
		if(minArea>0) {
			hql+=" and area>=?";
			objects.add(minArea);
		}
		if(maxArea>0) {
			hql+=" and area<=?";
			objects.add(maxArea);
		}
		if(minPrice>0) {
			hql+=" and price>=?";
			objects.add(minPrice);
		}
		if(maxPrice>0) {
			hql+=" and price<=?";
			objects.add(maxPrice);
		}
		hql+=" order by createDate desc";
		Object [] objs=new Object[objects.size()];
		for(int i=0; i<objects.size(); i++)
			objs[i]=objects.get(i);
		return getHibernateTemplate().find(hql, objs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> finidRoomLimit(int limit) {
		String hql="from Room order by createDate desc";
		List<Room> rooms=getHibernateTemplate().executeFind(new HibernateCallback<List<Room>>() {
			@Override
			public List<Room> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				query.setFirstResult(0);
				query.setMaxResults(limit);
				return query.list();
			}
		});
		return rooms;
	}

}
