package com.xwkj.booking.servlet;

import java.io.IOException;

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
import com.xwkj.booking.domain.Room;
import com.xwkj.booking.domain.User;
import com.xwkj.booking.service.PayManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.SMSService;

@WebServlet("/AlipayPayedServlet")
public class AlipayPayedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AlipayPayedServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bno=request.getParameter("out_trade_no");
		String notify_id=request.getParameter("notify_id");
		System.out.println(bno+" has been paeded");
		WebApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		ManagerTemplate managerTemplate=(ManagerTemplate)context.getBean("managerTemplate");
		Booking booking=managerTemplate.getBookingDao().findByBno(bno);
		//支付完成更新支付信息、支付宝服务器可能会发出多次请求、如果已经接受一次请求之后将屏蔽支付宝的请求
		if(AlipaySubmit.notifyVertify(notify_id)&&!booking.getPay()) {
			PayManager payManager=(PayManager)context.getBean("payManager");
			payManager.updatePayedState(bno);
			booking.setPay(true);
			managerTemplate.getBookingDao().update(booking);
			//支付成功后、房间的销售数量加1
			Room room=booking.getRoom();
			room.setSold(room.getSold()+1);
			managerTemplate.getRoomDao().update(room);
			//发送短息通知用户支付成功
			SMSService sms=(SMSService)context.getBean("SMSService");
			User user=booking.getUser();
			Pay pay=managerTemplate.getPayDao().findByBooking(booking);
			String value="#name#="+user.getUname()+"&#bno#="+booking.getBno()
				+"&#payDate#="+DateTool.formatDate(pay.getPayDate(), DateTool.DATE_HOUR_MINUTE_FORMAT_CN)+"&#amount#="+booking.getAmount();
			sms.send(user.getTelephone(), payManager.getPayedSMSTemplateID(), value);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
