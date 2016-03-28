package com.xwkj.booking.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xwkj.booking.dao.BookingDao;
import com.xwkj.booking.dao.PayDao;
import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.domain.Pay;
import com.xwkj.booking.domain.Room;
import com.xwkj.booking.domain.User;
import com.xwkj.booking.service.PayManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.SMSService;

@WebServlet("/WeChatPayedServlet")
public class WeChatPayedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WeChatPayedServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result="";
		BufferedReader reader=new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        while((line = reader.readLine())!=null){
            result+=line;
        }
        Document document=null;
		try {
			document=DocumentHelper.parseText(result);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if(document==null) {
			response.getWriter().print("<xml><return_code>FAIL</return_code></xml>");
			return;
		}
		Element root=document.getRootElement();
		String nonce_str=root.elementText("nonce_str");
		String return_code=root.elementText("return_code");
		String result_code=root.elementText("result_code");
		String out_trade_no=root.elementText("out_trade_no");
		String time_end=root.elementText("time_end");
		
		System.out.println(out_trade_no+" has been paeded at "+new Date());
		WebApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		PayManager payManager=(PayManager)context.getBean("payManager");
		ManagerTemplate managerTemplate=(ManagerTemplate)context.getBean("managerTemplate");
		BookingDao bookingDao=managerTemplate.getBookingDao();
		PayDao payDao=managerTemplate.getPayDao();
		Booking booking=bookingDao.findByBno(out_trade_no);
		//查询不到订单
		if(booking==null) {
			response.getWriter().print("<xml><return_code>FAIL</return_code></xml>");
			return;
		}
		//返回码存在失败信息
		if(return_code.equals("FAIL")||result_code.equals("FAIL")) {
			response.getWriter().print("<xml><return_code>FAIL</return_code></xml>");
			return;
		}
		Pay pay=payDao.findByBooking(booking);
		//比对随机字符串
		if(nonce_str.equals(pay.getWechatNonce())) {
			//更新订单支付信息
			booking.setPay(true);
			bookingDao.update(booking);
			pay.setPayed(true);
			pay.setPayDate(DateTool.transferDate(time_end, "yyyyMMddhhmmss"));
			payDao.update(pay);
			//支付成功后、房间的销售数量加1
			Room room=booking.getRoom();
			room.setSold(room.getSold()+1);
			managerTemplate.getRoomDao().update(room);
			//发送短息通知用户支付成功
			SMSService sms=(SMSService)context.getBean("SMSService");
			User user=booking.getUser();
			String value="#name#="+user.getUname()+"&#bno#="+booking.getBno()
				+"&#payDate#="+DateTool.formatDate(pay.getPayDate(), DateTool.DATE_HOUR_MINUTE_FORMAT_CN)+"&#amount#="+booking.getAmount();
			sms.send(user.getTelephone(), payManager.getPayedSMSTemplateID(), value);
			sms.send(payManager.getAdminTelephone(), payManager.getPayedSMSTemplateID(), value);
			response.getWriter().print("<xml><return_code>SUCCESS</return_code></xml>");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
