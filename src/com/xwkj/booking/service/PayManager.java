package com.xwkj.booking.service;

import com.xwkj.booking.bean.PayBean;

public interface PayManager {
	
	String getPayedSMSTemplateID();
	
	String getAdminTelephone();
	
	/**
	 * 通过订单获取支付信息
	 * 如果没有支付信息就创建一个新的支付信息
	 * @param bno
	 * @return
	 */
	PayBean getPayByBno(String bno);
	
	/**
	 * 写入签名
	 * @param bno
	 * @param sign
	 */
	void writeSign(String bno, String sign);
	
	/**
	 * 更新支付状态为已支付
	 * @param bno
	 * @param sign
	 */
	void updatePayedState(String bno);
}
