package com.objectway.stage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objectway.stage.manager.UserManager;
import com.objectway.stage.model.UserServiceBean;

@Component
public class UserService {
	@Autowired
	UserManager userManager;

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public UserServiceBean register(UserServiceBean user) {
		return userManager.addUser(user);
	}
	
	public boolean login(UserServiceBean user) {
		return userManager.matches(user);
	}
}
