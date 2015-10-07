package com.xwkj.booking.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.xwkj.booking.bean.TelephoneVerificationCode;
import com.xwkj.booking.bean.UserBean;
import com.xwkj.booking.domain.User;
import com.xwkj.booking.service.UserManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.HttpRequestUtil;
import com.xwkj.common.util.MathTool;
import com.xwkj.common.util.RandomValidateCode;

import net.sf.json.JSONObject;

public class UserManagerImpl extends ManagerTemplate implements UserManager {
	
	//验证时长
	private int verificationTimeout;
	//短信api发送地址
	private String SMSUrl;
	//短信API密钥
	private String SMSKey;
	//短信模板ID
	private int SMSTemplateID;
	//安全码错误提示
	private String securityCodeWrongTip;
	
	private String FailedReasonNoSMS;
	private String FailedReasonTimeout;
	private String FailedReasopnVerificationCodeWrong;
	private String FailedResionTelephoneWrong;
	private String FailedReasionSystemError;
	private String FailedReasionTelephoneNotExsit;

	public int getVerificationTimeout() {
		return verificationTimeout;
	}

	public void setVerificationTimeout(int verificationTimeout) {
		this.verificationTimeout = verificationTimeout;
	}

	public String getSMSUrl() {
		return SMSUrl;
	}

	public void setSMSUrl(String sMSUrl) {
		SMSUrl = sMSUrl;
	}

	public String getSMSKey() {
		return SMSKey;
	}

	public void setSMSKey(String sMSKey) {
		SMSKey = sMSKey;
	}

	public int getSMSTemplateID() {
		return SMSTemplateID;
	}

	public void setSMSTemplateID(int sMSTemplateID) {
		SMSTemplateID = sMSTemplateID;
	}

	public String getSecurityCodeWrongTip() {
		return securityCodeWrongTip;
	}

	public void setSecurityCodeWrongTip(String securityCodeWrongTip) {
		this.securityCodeWrongTip = securityCodeWrongTip;
	}

	public String getFailedReasonNoSMS() {
		return FailedReasonNoSMS;
	}

	public void setFailedReasonNoSMS(String failedReasonNoSMS) {
		FailedReasonNoSMS = failedReasonNoSMS;
	}

	public String getFailedReasonTimeout() {
		return FailedReasonTimeout;
	}

	public void setFailedReasonTimeout(String failedReasonTimeout) {
		FailedReasonTimeout = failedReasonTimeout;
	}

	public String getFailedReasopnVerificationCodeWrong() {
		return FailedReasopnVerificationCodeWrong;
	}

	public void setFailedReasopnVerificationCodeWrong(String failedReasopnVerificationCodeWrong) {
		FailedReasopnVerificationCodeWrong = failedReasopnVerificationCodeWrong;
	}

	public String getFailedResionTelephoneWrong() {
		return FailedResionTelephoneWrong;
	}

	public void setFailedResionTelephoneWrong(String failedResionTelephoneWrong) {
		FailedResionTelephoneWrong = failedResionTelephoneWrong;
	}

	public String getFailedReasionSystemError() {
		return FailedReasionSystemError;
	}

	public void setFailedReasionSystemError(String failedReasionSystemError) {
		FailedReasionSystemError = failedReasionSystemError;
	}

	public String getFailedReasionTelephoneNotExsit() {
		return FailedReasionTelephoneNotExsit;
	}

	public void setFailedReasionTelephoneNotExsit(String failedReasionTelephoneNotExsit) {
		FailedReasionTelephoneNotExsit = failedReasionTelephoneNotExsit;
	}

	@Override
	public String addUser(String telephone, String uname, String password, String tid) {
		User user=new User();
		user.setTelephone(telephone);
		user.setUname(uname);
		user.setPassword(password);
		return userDao.save(user);
	}

	@Override
	public boolean deleteUser(String uid) {
		User user=userDao.get(uid);
		userDao.delete(user);
		return true;
	}

	@Override
	public UserBean getUser(String uid) {
		User user=userDao.get(uid);
		if(user!=null)
			return new UserBean(user);
		return null;
	}

	@Override
	public boolean login(String telephone, String password, HttpSession session) {
		User user=userDao.findUserByTelephone(telephone);
		if(user==null)
			return false;
		if(user.getPassword().equals(password)) {
			UserBean userBean=new UserBean(user);
			session.setAttribute(USER_FLAG, userBean);
			return true;
		}
		return false;
	}
	
	@Override 
	public void logout(HttpSession session) {
		session.removeAttribute(USER_FLAG);
	}

	@Override
	public UserBean checkSession(HttpSession session) {
		if(session.getAttribute(UserManager.USER_FLAG)==null)
			return null;
		return (UserBean)session.getAttribute(UserManager.USER_FLAG);
	}
	
	@Override
	public boolean modifyUser(String uid, String uname, String password) {
		User user=userDao.get(uid);
		if(user==null)
			return false;
		user.setUname(uname);
		user.setPassword(password);
		userDao.update(user);
		return true;
	}

	@Override
	public Map<String, Object> getVerificationCode(String telephone, String code, HttpSession session) throws UnsupportedEncodingException {
		Map<String, Object> data=new HashMap<String, Object>();
		if(userDao.findUserByTelephone(telephone)!=null) {
			data.put("hasTelephone", true);
			return data;
		}
		boolean send=false;
		String securityCode=(String)session.getAttribute(RandomValidateCode.RANDOMCODEKEY);
		System.out.println("Security Code Check: server->"+securityCode+", user->"+code);
		//安全码不对不能发短信，以免有人恶意发短信
		if(securityCode==null||!securityCode.equalsIgnoreCase(code)) {
			data.put("send", send);
			data.put("reason", securityCodeWrongTip);
			return data;
		}
		data.put("verificationTimeout", verificationTimeout);
		String verificationCode=String.valueOf(MathTool.getRandom(999999));
		String value="#code#="+verificationCode+"&#minutes#="+(verificationTimeout/60);
		String url=SMSUrl+"?mobile="+telephone+"&tpl_id="+SMSTemplateID+"&tpl_value="+URLEncoder.encode(value, "UTF-8")+"&key="+SMSKey;
		JSONObject result=JSONObject.fromObject(HttpRequestUtil.httpRequest(url));
		if(Integer.parseInt(result.get("error_code").toString())==0) 
			send=true;
		data.put("reason", result.get("reason"));
//		boolean send=true;
//		String verificationCode="";
		data.put("send", send);
		TelephoneVerificationCode telephoneVerificationCode=new TelephoneVerificationCode(telephone, verificationCode, new Date());
		if(send) 
			session.setAttribute(VERIFICATION_CODE, telephoneVerificationCode);
		return data;
	}

	@Override
	public Map<String, Object> register(String telephone, String uname, String password, String verificationCode, HttpSession session) {
		Map<String, Object> data=new HashMap<String, Object>();
		//session中没有验证码返回报名失败
		if(session.getAttribute(VERIFICATION_CODE)==null) {
			data.put("success", false);
			data.put("reason", FailedReasonNoSMS);
			return data;
		}
		TelephoneVerificationCode	telephoneVerificationCode=(TelephoneVerificationCode)session.getAttribute(VERIFICATION_CODE);
		Date now=new Date();
		//时间超过验证时长verificationTimeout返回报名失败
		if((now.getTime()-telephoneVerificationCode.getTime().getTime())/1000>verificationTimeout) {
			data.put("success", false);
			data.put("reason", FailedReasonTimeout);
			return data;
		}
		//验证码不正确返回失败
		if(!verificationCode.equals(telephoneVerificationCode.getCode())) {
			data.put("success", false);
			data.put("reason", FailedReasopnVerificationCodeWrong);
			return data;
		}
		if(!telephone.equals(telephoneVerificationCode.getTelephone())) {
			data.put("success", false);
			data.put("reason", FailedResionTelephoneWrong);
			return data;
		}
		//通过验证，开始处理注册信息
		User user=new User();
		user.setTelephone(telephone);
		user.setUname(uname);
		user.setPassword(password);
		user.setRegisterDate(new Date());
		String uid=userDao.save(user);
		if(uid==null) {
			data.put("success", false);
			data.put("reason", FailedReasionSystemError);
			return data;
		}
		data.put("success", true);
		data.put("uid", uid);
		return data;
	}

	@Override
	public Map<String, Object> getModifyVerificationCode(String telephone, String code, HttpSession session) throws UnsupportedEncodingException {
		Map<String, Object> data=new HashMap<String, Object>();
		if(userDao.findUserByTelephone(telephone)==null) {
			data.put("hasTelephone", false);
			return data;
		}
		data.put("hasTelephone", true);
		boolean send=false;
		String securityCode=(String)session.getAttribute(RandomValidateCode.RANDOMCODEKEY);
		System.out.println("Security Code Check: server->"+securityCode+", user->"+code);
		//安全码不对不能发短信，以免有人恶意发短信
		if(securityCode==null||!securityCode.equalsIgnoreCase(code)) {
			data.put("send", send);
			data.put("reason", securityCodeWrongTip);
			return data;
		}
		data.put("verificationTimeout", verificationTimeout);
		String verificationCode=String.valueOf(MathTool.getRandom(999999));
		String value="#code#="+verificationCode+"&#minutes#="+(verificationTimeout/60);
		String url=SMSUrl+"?mobile="+telephone+"&tpl_id="+SMSTemplateID+"&tpl_value="+URLEncoder.encode(value, "UTF-8")+"&key="+SMSKey;
		JSONObject result=JSONObject.fromObject(HttpRequestUtil.httpRequest(url));
		if(Integer.parseInt(result.get("error_code").toString())==0) 
			send=true;
		data.put("reason", result.get("reason"));
//		boolean send=true;
//		String verificationCode="";
		data.put("send", send);
		TelephoneVerificationCode telephoneVerificationCode=new TelephoneVerificationCode(telephone, verificationCode, new Date());
		if(send) 
			session.setAttribute(MODIFY_VERIFICATION_CODE, telephoneVerificationCode);
		return data;
	}

	@Override
	public Map<String, Object> validateModifyVerificationCode(String telephone, String verificationCode, HttpSession session) {
		Map<String, Object> data=new HashMap<String, Object>();
		//session中没有验证码返回报名失败
		if(session.getAttribute(MODIFY_VERIFICATION_CODE)==null) {
			data.put("success", false);
			data.put("reason", FailedReasonNoSMS);
			return data;
		}
		TelephoneVerificationCode	telephoneVerificationCode=(TelephoneVerificationCode)session.getAttribute(MODIFY_VERIFICATION_CODE);
		Date now=new Date();
		//时间超过验证时长verificationTimeout返回报名失败
		if((now.getTime()-telephoneVerificationCode.getTime().getTime())/1000>verificationTimeout) {
			data.put("success", false);
			data.put("reason", FailedReasonTimeout);
			return data;
		}
		//验证码不正确返回失败
		if(!verificationCode.equals(telephoneVerificationCode.getCode())) {
			data.put("success", false);
			data.put("reason", FailedReasopnVerificationCodeWrong);
			return data;
		}
		//验证手机号码
		if(!telephone.equals(telephoneVerificationCode.getTelephone())) {
			data.put("success", false);
			data.put("reason", FailedResionTelephoneWrong);
			return data;
		}
		User user=userDao.findUserByTelephone(telephone);
		if(user==null) {
			data.put("success", false);
			data.put("reason", FailedResionTelephoneWrong);
			return data;
		}
		//通过验证
		data.put("success", true);
		data.put("uid", user.getUid());
		data.put("uname", user.getUname());
		if(session.getAttribute(UserManager.USER_FLAG)!=null) {
			UserBean userBean=new UserBean(user);
			session.setAttribute(USER_FLAG, userBean);
		}
		return data;
	}

}
