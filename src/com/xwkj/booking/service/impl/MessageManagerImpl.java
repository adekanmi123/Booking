package com.xwkj.booking.service.impl;

import java.util.Date;
import java.util.List;

import com.xwkj.booking.bean.MessageBean;
import com.xwkj.booking.domain.Message;
import com.xwkj.booking.service.MessageManager;
import com.xwkj.booking.service.util.ManagerTemplate;

public class MessageManagerImpl extends ManagerTemplate implements MessageManager {

	@Override
	public String addMessage(String name, String email, String telephone, String content) {
		Message message=new Message();
		message.setName(name);
		message.setEmail(email);
		message.setTelephone(telephone);
		message.setContent(content);
		message.setDate(new Date());
		message.setLooked(false);
		return messageDao.save(message);
	}

	@Override
	public MessageBean getMessage(String mid) {
		Message message=messageDao.get(mid);
		if(message==null)
			return null;
		return new MessageBean(message);
	}
	
	@Override
	public int getMessagesCount(Date start, Date end, String name, String email, String telephone, int read) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MessageBean> searchMessages(Date start, Date end, String name, String email, String telephone, int looked,
			int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

}
