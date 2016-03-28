package com.xwkj.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

public class SMSService {
	
	//短信api发送地址
	private String SMSUrl;
	//短信API密钥
	private String SMSKey;

	public void setSMSUrl(String sMSUrl) {
		SMSUrl = sMSUrl;
	}

	public void setSMSKey(String sMSKey) {
		SMSKey = sMSKey;
	}
	
	public JSONObject send(String telephone, String templateId, String templateValue)  {
		JSONObject result=null;
		try {
			String url = SMSUrl+"?mobile="+telephone+"&tpl_id="+templateId+"&tpl_value="
					+URLEncoder.encode(templateValue, "UTF-8")+"&key="+SMSKey;
			result=JSONObject.fromObject(HttpRequestUtil.httpRequest(url));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
