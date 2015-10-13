package com.alipay.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;

public class AlipayService {

	//常量
    //支付宝提供给商户的服务接入网关URL(新)
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	//定义即时到帐服务
	public static final String service="create_direct_pay_by_user";
	//支付类型，必填，不能修改
	public static final String payment_type = "1";

	//服务器异步通知页面路径，需http://格式的完整路径，不能加?id=123这类自定义参数
	private String notify_url;
	//页面跳转同步通知页面路径，需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
	private String return_url ;
	
	//对象初始化后手工设置的变量
	//商户订单号，商户网站订单系统中唯一订单号，必填
	private String out_trade_no;
	//订单名称，必填
	private String subject;
	//付款金额，必填
	private String total_fee;
	//订单描述
	private String body;
	//商品展示地址，需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html
	private String show_url;
	//防钓鱼时间戳，若要使用请调用类文件submit中的query_timestamp函数
	private String anti_phishing_key = "";
	//客户端的IP地址，非局域网的外网IP地址，如：221.0.0.1
	private String exter_invoke_ip = "";

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}

	public void setAnti_phishing_key(String anti_phishing_key) {
		this.anti_phishing_key = anti_phishing_key;
	}

	public void setExter_invoke_ip(String exter_invoke_ip) {
		this.exter_invoke_ip = exter_invoke_ip;
	}
	
	public String pay() throws Exception {
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<>();
		sParaTemp.put("service", service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_email", AlipayConfig.seller_email);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		 //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AlipayConfig.input_charset);
        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(ALIPAY_GATEWAY_NEW+"_input_charset="+AlipayConfig.input_charset);
        HttpResponse response = httpProtocolHandler.execute(request, "", "");
        if (response == null) {
            return null;
        }
        String strResult = response.getStringResult();
        return strResult;
	}
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayConfig.sign_type);
        return sPara;
    }
    
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(AlipayConfig.sign_type.equals("MD5") ) {
        	mysign = MD5.sign(prestr, AlipayConfig.key, AlipayConfig.input_charset);
        }
        return mysign;
    }
	
	/**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }
}
