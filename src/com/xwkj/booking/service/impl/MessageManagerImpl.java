package com.xwkj.booking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xwkj.booking.bean.MessageBean;
import com.xwkj.booking.domain.Message;
import com.xwkj.booking.service.MessageManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.MailService;
import com.xwkj.common.util.RandomValue;
import com.xwkj.common.util.SMSService;

import net.sf.json.JSONObject;

public class MessageManagerImpl extends ManagerTemplate implements MessageManager {
	
	private String SMTPServer;
	private String username;
	private String password;
	
	private int SMSTemplateID;
	
	private String replyMessageSubject;
	private String replyMessageHead;
	private String replyMessageFoot;

	public void setSMTPServer(String sMTPServer) {
		SMTPServer = sMTPServer;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSMSTemplateID(int sMSTemplateID) {
		this.SMSTemplateID = sMSTemplateID;
	}

	public void setReplyMessageSubject(String replyMessageSubject) {
		this.replyMessageSubject = replyMessageSubject;
	}

	public void setReplyMessageHead(String replyMessageHead) {
		this.replyMessageHead = replyMessageHead;
	}

	public void setReplyMessageFoot(String replyMessageFoot) {
		this.replyMessageFoot = replyMessageFoot;
	}

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
	public int getMessagesCount(String start, String end, String name, String email, String telephone, int looked) {
		boolean _looked=false, showAll=false;
		if(looked==-1)
			showAll=true;
		else
			_looked= (looked==1)? true: false;
		Date startDate=null, endDate=null;
		if(!start.equals("")) 
			startDate=DateTool.transferDate(start+" 00:00:00", DateTool.DATE_HOUR_MINUTE_FORMAT);
		if(!end.equals("")) 
			endDate=DateTool.transferDate(end+" 23:59:59", DateTool.DATE_HOUR_MINUTE_FORMAT);
		return messageDao.getMessagesCount(startDate, endDate, name, email, telephone, showAll, _looked);
	}

	@Override
	public List<MessageBean> searchMessages(String start, String end, String name, String email, String telephone, int looked,
			int page, int pageSize) {
		boolean _looked=false, showAll=false;
		if(looked==-1)
			showAll=true;
		else
			_looked= (looked==1)? true: false;
		List<MessageBean> messages=new ArrayList<>();
		int offset=(page-1)*pageSize;
		Date startDate=null, endDate=null;
		if(!start.equals("")) 
			startDate=DateTool.transferDate(start+" 00:00:00", DateTool.DATE_HOUR_MINUTE_FORMAT);
		if(!end.equals("")) 
			endDate=DateTool.transferDate(end+" 23:59:59", DateTool.DATE_HOUR_MINUTE_FORMAT);
		for(Message message: messageDao.findByPage(startDate, endDate, name, email, telephone, showAll, _looked, offset, pageSize))
			messages.add(new MessageBean(message));
		return messages;
	}

	@Override
	public void looked(String mid, boolean looked) {
		Message message=messageDao.get(mid);
		message.setLooked(looked);
		messageDao.update(message);
	}

	/**
	 * 测试方法 随机生成n个留言信息
	 * @param n
	 */
	public void randomCreateMessages(int n) {
		for(int i=0;i<n;i++) {
			Map<String, String> person=RandomValue.getPersonInfo();
			Message message=new Message();
			message.setName(person.get("name"));
			message.setEmail(person.get("email"));
			message.setTelephone(person.get("tel"));
			message.setContent(person.get("road"));
			message.setDate(DateTool.randomDate("2015-09-01", "2015-10-10"));
			message.setLooked(false);
			messageDao.save(message);
		}
	}

	@Override
	public boolean replyByEmail(String mid, String reply) {
		Message message=messageDao.get(mid);
		MailService service=new MailService();
		service.setSmtpServer(SMTPServer);
		service.setUsername(username);
		service.setPassword(password);
		service.setTo(message.getEmail());
		service.setFrom(username);
		service.setSubject(replyMessageSubject);
		service.setContent(replyMessageHead+"\n"+reply+"\n"+replyMessageFoot);
		return service.send();
	}

	@Override
	public boolean replyBySMS(String mid, String reply) {
		Message message=messageDao.get(mid);
		SMSService sms=(SMSService)WebApplicationContextUtils.getWebApplicationContext(WebContextFactory.get().getServletContext()).getBean("SMSService");
		String value="#code#="+reply;
		JSONObject result=sms.send(message.getTelephone(), SMSTemplateID, value);
		if(Integer.parseInt(result.get("error_code").toString())==0) 
			return true;
		else
			System.out.println(result.get("reason"));
		return false;
	}

}
