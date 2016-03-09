package com.xwkj.booking.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.WebContextFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wechat.pay.util.RequestHandler;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Pay;
import com.xwkj.booking.service.WeChatPayManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.HttpRequestUtil;

public class WeChatPayManagerImpl extends ManagerTemplate implements WeChatPayManager {
	
	private String createOrderURL;
	private String notifyDomain;
	private String appid;
	private String appsecret ;
	private String partner;
	private String partnerkey;
	
	public void setCreateOrderURL(String createOrderURL) {
		this.createOrderURL = createOrderURL;
	}

	public void setNotifyDomain(String notifyDomain) {
		this.notifyDomain = notifyDomain;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public void setPartnerkey(String partnerkey) {
		this.partnerkey = partnerkey;
	}

	public String getAppid() {
		return appid;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public String getNotifyDomain() {
		return notifyDomain;
	}

	@Override
	public Map<String, Object> createNative(String bno) throws IOException {
		Booking booking=bookingDao.findByBno(bno);
		if(booking.getPay()||booking.getTimeout()) {
			return null;
		}
		HttpServletRequest request= WebContextFactory.get().getHttpServletRequest();
		HttpServletResponse response= WebContextFactory.get().getHttpServletResponse();
		String trade_type = "NATIVE";
		//商户号
		String mch_id = partner;
		//随机数 
		String nonce_str=UUID.randomUUID().toString().substring(0,8);
		//商品描述根据情况修改
		String body="于"+DateTool.formatDate(booking.getCheckin(), DateTool.YEAR_MONTH_DATE_FORMAT_CN)+"至"
				+DateTool.formatDate(booking.getCheckin(), DateTool.YEAR_MONTH_DATE_FORMAT_CN)+"入住，共"
				+booking.getDays()+"晚";
		//商户订单号
		String out_trade_no = booking.getBno();
		//总金额以分为单位，不带小数点
		int total_fee=(int) Math.round(booking.getAmount()*100);
		//订单生成的机器 IP
		String spbill_create_ip = request.getRemoteAddr();
		//这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url ="http://"+notifyDomain+"/WeChatPayedServlet";
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);  
		packageParams.put("mch_id", mch_id);  
		packageParams.put("nonce_str", nonce_str);  
		packageParams.put("body", body);  
		packageParams.put("out_trade_no", out_trade_no);  
		packageParams.put("total_fee", String.valueOf(total_fee));  
		packageParams.put("spbill_create_ip", spbill_create_ip);  
		packageParams.put("notify_url", notify_url);  
		packageParams.put("trade_type", trade_type);  
		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appid, appsecret, partnerkey);
		String sign = reqHandler.createSign(packageParams);
		String xml="<xml>"+
					"<appid>"+appid+"</appid>"+
					"<mch_id>"+mch_id+"</mch_id>"+
					"<nonce_str>"+nonce_str+"</nonce_str>"+
					"<sign>"+sign+"</sign>"+
					"<body><![CDATA["+body+"]]></body>"+
					"<out_trade_no>"+out_trade_no+"</out_trade_no>"+
					"<total_fee>"+total_fee+"</total_fee>"+
					"<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
					"<notify_url>"+notify_url+"</notify_url>"+
					"<trade_type>"+trade_type+"</trade_type>"+
				"</xml>";
		String result=HttpRequestUtil.postWithString(createOrderURL, xml);
		result=new String(result.getBytes("gbk") , "UTF-8");
		Document document=null;
		try {
			document=DocumentHelper.parseText(result);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if(document==null)
			return null;
		Element root=document.getRootElement();
		if(root.elementText("return_code").equals("FAIL"))
			return null;
		//把随机生成码写入订单中，以便微信支付成功后后台调用
		Pay pay=payDao.findByBooking(booking);
		pay.setPayWay("wechat");
		pay.setWechatNonce(nonce_str);
		payDao.update(pay);
		Map<String, Object> data=new HashMap<>();
		data.put("codeUrl", root.elementText("code_url"));
		return data;
	}

}
