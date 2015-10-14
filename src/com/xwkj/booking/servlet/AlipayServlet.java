package com.xwkj.booking.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alipay.service.AlipayConfig;
import com.alipay.service.AlipaySubmit;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.service.PayManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;

/**
 * Servlet implementation class AlipayServlet
 */
@WebServlet("/AlipayServlet")
public class AlipayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private String task;
    
    public AlipayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		task=request.getParameter("task");
		switch (task) {
		case "pay":
			pay(request,response);
			break;

		default:
			break;
		}
	}

	private void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String bno=request.getParameter("bno");
		WebApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		ManagerTemplate manager=(ManagerTemplate)context.getBean("managerTemplate");
		PayManager pay=(PayManager)context.getBean("payManager");
		Booking booking=manager.getBookingDao().findByBno(bno);
		//支付类型
		String payment_type = "1";
		//服务器异步通知页面路径
		String notify_url = "http://booking.xwzx198.com/AlipayPayedServlet";
		//页面跳转同步通知页面路径
		String return_url = "http://booking.xwzx198.com/pay.html?bno="+bno;
		//商户订单号
		String out_trade_no = booking.getBno();
		//订单名称
		String subject = booking.getRoom().getRname();
		//付款金额
		String total_fee = booking.getAmount().toString();
		//订单描述
		String body="于"+DateTool.formatDate(booking.getCheckin(), DateTool.YEAR_MONTH_DATE_FORMAT_CN)+"至"
				+DateTool.formatDate(booking.getCheckin(), DateTool.YEAR_MONTH_DATE_FORMAT_CN)+"入住，共"
				+booking.getDays()+"晚，合计￥"+booking.getAmount()+"元";
		//商品展示地址
		String show_url = new String("http://booking.xwzx198.com/room.html?rid="+booking.getRoom().getRid());
		//防钓鱼时间戳
		String anti_phishing_key = "";
		//若要使用请调用类文件submit中的query_timestamp函数
		//客户端的IP地址
		String exter_invoke_ip = "";
		//非局域网的外网IP地址，如：221.0.0.1
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
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
		//建立请求
		Map<String, Object> data= AlipaySubmit.buildRequest(sParaTemp,"get","确认");
		System.out.println(data.get("sbHtml"));
		//把签名写入数据库
		pay.writeSign(bno, data.get("sign").toString());
		//跳转页面
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");  
		response.getWriter().print(data.get("sbHtml"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
