package com.objectway.stage.converter;

import java.util.ArrayList;
import java.util.List;

import com.objectway.stage.model.UserServiceBean;
import com.objectway.stage.viewbeans.UserViewBean;

public class UserViewConverter implements ServiceViewConverter<UserServiceBean, UserViewBean>{
	@Override
	public UserServiceBean viewToService(UserViewBean uv) {
		UserServiceBean us = new UserServiceBean();
		if(uv!=null) {
			us.setUsername(uv.getUsername());
			us.setPassword(uv.getPassword());
			us.setLoggedIn(uv.isLoggedIn());
		}
		return us;
	}
	
	@Override
	public UserViewBean serviceToView(UserServiceBean us) {
		UserViewBean uv = new UserViewBean();
		uv.setUsername(us.getUsername());
		uv.setPassword(us.getPassword());
		uv.setLoggedIn(us.isLoggedIn());
		return uv;
	}

	@Override
	public List<UserServiceBean> viewListToServiceList(List<UserViewBean> uvl) {
		List<UserServiceBean> usl = new ArrayList<>();
		uvl.forEach(uv -> usl.add(viewToService(uv)));
		return usl;
	}

	@Override
	public List<UserViewBean> serviceListToViewList(List<UserServiceBean> usl) {
		List<UserViewBean> uvl = new ArrayList<>();
		usl.forEach(us -> uvl.add(serviceToView(us)));
		return uvl;
	}
}
