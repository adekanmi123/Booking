package com.xwkj.booking.service;

import java.io.IOException;
import java.util.Map;

public interface WeChatPayManager {
	
	/**
	 * 供外部类调用appid
	 * @return
	 */
	String getAppid();
	
	/**
	 * 供外部类调用appsecret
	 * @return
	 */
	String getAppsecret(); 
	
	/**
	 * 供外部类调用域名
	 * @return
	 */
	String getNotifyDomain();
	
	/**
	 * 创建一个Native支付订单
	 * @param tno 
	 * @return code_url
	 * @throws IOException
	 */
	Map<String, Object>  createNative(String bno) throws IOException;
}
