package com.alipay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xwkj.common.util.HttpRequestUtil;

/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipaySubmit {

	// 字符编码格式 目前支持 gbk 或 utf-8
	public final static String input_charset = "utf-8";
	// 签名方式 不需修改
	public final static String sign_type = "MD5";
    //支付宝提供给商户的服务接入网关URL(新)
    private final static String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	//支付类型
	private final static String payment_type = "1";
	//防钓鱼时间戳，若要使用请调用类文件submit中的query_timestamp函数
	private final static String anti_phishing_key = "";
	//客户端的IP地址，非局域网的外网IP地址，如：221.0.0.1
	private final static String exter_invoke_ip = "";
    
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	private String partner;
	// 收款支付宝账号
	private String seller_email;
	// 商户的私钥
	private String key;
	//服务器异步通知页面路径
	private String notify_url;
	//页面跳转同步通知页面路径
	private String return_url;
	//商品展示地址
	private String show_url;
	
	public String getPartner() {
		return partner;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public String getKey() {
		return key;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public String getShow_url() {
		return show_url;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}

	/**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(sign_type.equals("MD5") ) {
        	mysign = MD5.sign(prestr, key, input_charset);
        }
        return mysign;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", sign_type);

        return sPara;
    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public Map<String, Object> buildRequest(int it_b_pay, String out_trade_no, String subject, double total_fee, String body) {
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", partner);
        sParaTemp.put("seller_email", seller_email);
        sParaTemp.put("_input_charset", input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url+out_trade_no);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", String.valueOf(total_fee));
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		sParaTemp.put("it_b_pay", it_b_pay+"m"); //支付超时
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW
                      + "_input_charset=" + input_charset + "\" method=\"" + "get"
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + "submit" + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
        
        Map<String, Object> data=new HashMap<>();
        data.put("sbHtml", sbHtml.toString());
        data.put("sign", sPara.get("sign"));
        return data;
    }
    
	/**
	 * 验证异步请求是否是支付宝发出的
	 * @param notify_id
	 * @return
	 */
	public boolean notifyVertify(String notify_id) {
		String url=ALIPAY_GATEWAY_NEW+"service=notify_verify&partner="+partner+"&notify_id="+notify_id;
		String result=HttpRequestUtil.httpRequest(url);
		if(result.equals("true"))
			return true;
		return false;
	}
}
