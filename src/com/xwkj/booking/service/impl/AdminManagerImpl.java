package com.xwkj.booking.service.impl;

import javax.servlet.http.HttpSession;

import com.xwkj.booking.service.AdminManager;
import com.xwkj.booking.service.util.ManagerTemplate;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AdminManagerImpl extends ManagerTemplate implements AdminManager {
	private String accounts;
	private JSONArray admins;

	public void setAccounts(String accounts) {
		this.accounts = accounts;
		admins=JSONArray.fromObject(this.accounts);
	}

	@Override
	public boolean login(String username, String password, HttpSession session) {
		for(int i=0; i<admins.size(); i++) {
			JSONObject admin=(JSONObject) admins.get(i);
			if(username.equals(admin.getString("name"))&&password.equals(admin.getString("password"))) {
				session.setAttribute(ADMIN_FLAG, username);
				return true;
			}	
		}
		
		return false;
	}

	@Override
	public String checkSession(HttpSession session) {
		if(session.getAttribute(ADMIN_FLAG)==null)
			return null;
		return (String)session.getAttribute(ADMIN_FLAG);
	}
}