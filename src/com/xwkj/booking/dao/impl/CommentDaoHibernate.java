package com.xwkj.booking.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.xwkj.booking.dao.CommentDao;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Comment;
import com.xwkj.booking.domain.Room;
import com.xwkj.common.hibernate3.support.PageHibernateDaoSupport;

public class CommentDaoHibernate extends PageHibernateDaoSupport implements CommentDao {

	@Override
	public Comment get(String cid) {
		return getHibernateTemplate().get(Comment.class, cid);
	}

	@Override
	public String save(Comment comment) {
		return (String)getHibernateTemplate().save(comment);
	}

	@Override
	public void update(Comment comment) {
		getHibernateTemplate().update(comment);
	}

	@Override
	public void delete(Comment comment) {
		getHibernateTemplate().delete(comment);
	}

	@Override
	public Comment findByBooking(Booking booking) {
		@SuppressWarnings("unchecked")
		List<Comment> comments=getHibernateTemplate().find("from Comment where booking=?", booking);
		if(comments.size()>0)
			return comments.get(0);
		return null;
	}

	@Override
	public int getCommentsCount(Date start, Date end, String rname, boolean showAll, boolean enable) {
		String hql="select count(*) from Comment where content!=null ";
		List<Object> objects=new ArrayList<>();
		if(start!=null) {
			hql+=" and commentDate>=?";
			objects.add(start);
		}
		if(end!=null) {
			hql+=" and commentDate<=?";
			objects.add(end);
		}
		if(rname!=null&&!rname.equals("")) {
			hql+=" and booking.room.rname like ?";
			objects.add("%"+rname+"%");
		}
		if(!showAll) {
			hql+=" and enable=?";
			objects.add(enable);
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
	public List<Comment> findByPage(Date start, Date end, String rname, boolean showAll, boolean enable, int offset, int pageSize) {
		String hql="from Comment where content!=null ";
		List<Object> objects=new ArrayList<>();
		if(start!=null) {
			hql+=" and commentDate>=?";
			objects.add(start);
		}
		if(end!=null) {
			hql+=" and commentDate<=?";
			objects.add(end);
		}
		if(rname!=null&&!rname.equals("")) {
			hql+=" and booking.room.rname like ?";
			objects.add("%"+rname+"%");
		}
		if(!showAll) {
			hql+=" and enable=?";
			objects.add(enable);
		}
		hql+=" order by commentDate desc";
		Object [] objs=new Object[objects.size()];
		for(int i=0; i<objects.size(); i++)
			objs[i]=objects.get(i);
		return findByPage(hql, objs, offset, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Comment> findEnableCommentLimit(int limit) {
		String hql="from Comment where enable=true order by commentDate desc";
		return getHibernateTemplate().executeFind(new HibernateCallback<List<Room>>() {
			@Override
			public List<Room> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				query.setFirstResult(0);
				query.setMaxResults(limit);
				return query.list();
			}
		});
	}

}
