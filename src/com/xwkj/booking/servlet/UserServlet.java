package com.xwkj.booking.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xwkj.booking.bean.UserBean;
import com.xwkj.booking.dao.UserDao;
import com.xwkj.booking.domain.User;

import net.sf.json.JSONObject;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private String task;
	private WebApplicationContext context;
       
    public UserServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		context=WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		task=request.getParameter("task");
		switch (task) {
		case "login":
			login(request, response);
			break;

		default:
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		String telephone=request.getParameter("telephone");
		String password=request.getParameter("password");
		UserDao userDao=(UserDao)context.getBean("userDao");
		User user=userDao.findUserByTelephone(telephone);
		JSONObject data=new JSONObject();
		if(user==null) {
			data.put("exsit", false);
			response.getWriter().print(data.toString());
			return;
		}
		if(!user.getPassword().equals(password)) {
			data.put("exsit", true);
			data.put("success", false);
			response.getWriter().print(data.toString());
			return;
		}
		data.put("exsit", true);
		data.put("success", true);
		data.put("user", new UserBean(user));
		response.getWriter().print(data.toString());
	}

}
