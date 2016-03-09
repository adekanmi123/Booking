package com.xwkj.booking.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alipay.service.AlipaySubmit;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Pay;
import com.xwkj.booking.service.BookingManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;

@WebServlet("/AlipayServlet")
public class AlipayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private String task;
    
    public AlipayServlet() {
        super();
    }

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
		BookingManager bookingManager=(BookingManager)context.getBean("bookingManager");
		ManagerTemplate manager=(ManagerTemplate)context.getBean("managerTemplate");
		Booking booking=manager.getBookingDao().findByBno(bno);
		AlipaySubmit alipaySubmit=(AlipaySubmit)context.getBean("AlipaySubmit");
		//订单描述
		String body="于"+DateTool.formatDate(booking.getCheckin(), DateTool.YEAR_MONTH_DATE_FORMAT_CN)+"至"
				+DateTool.formatDate(booking.getCheckin(), DateTool.YEAR_MONTH_DATE_FORMAT_CN)+"入住，共"
				+booking.getDays()+"晚，合计￥"+booking.getAmount()+"元";
		//超时时长
		int minutes=bookingManager.getPayTimeOut()-DateTool.minutesBetween(booking.getCreateDate(), new Date());
		//建立请求
		Map<String, Object> data= alipaySubmit.buildRequest(minutes, booking.getBno(), booking.getRoom().getRname(), booking.getAmount(), body);
		//把签名写入数据库
		Pay pay=manager.getPayDao().findByBooking(booking);
		pay.setSign(data.get("sign").toString());
		pay.setPayWay("alipay");
		manager.getPayDao().update(pay);
		//跳转页面
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");  
		response.getWriter().print(data.get("sbHtml"));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
