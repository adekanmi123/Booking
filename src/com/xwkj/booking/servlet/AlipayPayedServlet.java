package com.xwkj.booking.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xwkj.booking.domain.Booking;
import com.xwkj.booking.service.util.ManagerTemplate;

/**
 * Servlet implementation class AlipayPayedServlet
 */
@WebServlet("/AlipayPayedServlet")
public class AlipayPayedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlipayPayedServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("getRequestURL: "+request.getRequestURL());
		System.out.println("getRequestURI: "+request.getRequestURI());
		System.out.println("getQueryString: "+request.getQueryString());
		System.out.println("getRemoteAddr: "+request.getRemoteAddr());
		System.out.println("getRemoteHost: "+request.getRemoteHost());
		System.out.println("getRemotePort: "+request.getRemotePort());
		System.out.println("getRemoteUser: "+request.getRemoteUser());
		System.out.println("getLocalAddr: "+request.getLocalAddr());
		System.out.println("getLocalName: "+request.getLocalName());
		System.out.println("getLocalPort: "+request.getLocalPort());
		System.out.println("getMethod: "+request.getMethod());
		System.out.println("-------request.getParamterMap()-------");
		//得到请求的参数Map，注意map的value是String数组类型

		Map<String, String[]> map = request.getParameterMap();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
		String[] values = (String[]) map.get(key);
		    for (String value : values)
		        System.out.println(key+"="+value);
		}
		System.out.println("--------request.getHeader()--------");
		//得到请求头的name集合

		Enumeration<String> em = request.getHeaderNames();
		while (em.hasMoreElements()) {
		    String name = (String) em.nextElement();
		    String value = request.getHeader(name);
		    System.out.println(name+"="+value);
		}
		
		//支付完成更新booking
		String bno=request.getParameter("out_trade_no");
		ManagerTemplate manager=(ManagerTemplate)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("managerTemplate");
		Booking booking=manager.getBookingDao().findByBno(bno);
		booking.setPayed(true);
		booking.setPayDate(new Date());
		manager.getBookingDao().update(booking);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
