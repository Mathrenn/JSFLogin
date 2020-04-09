package com.objectway.stage.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objectway.stage.converter.UserEntityConverter;
import com.objectway.stage.entity.UserEntity;
import com.objectway.stage.model.UserServiceBean;
import com.objectway.stage.repository.HashUtils;
import com.objectway.stage.repository.UserRepository;

@Component
public class UserManager {
	private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

	private static final UserEntityConverter converter = new UserEntityConverter();

	@Autowired
	private UserRepository userRepository;

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserServiceBean addUser(UserServiceBean user) {
		logger.info("Initialized method UserManager.addUser()");
		try {
			UserEntity ue = converter.serviceToEntity(user);
			userRepository.register(ue);
			logger.info("Transaction completed");
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("Transaction failed "+e.getClass().getSimpleName());
			return new UserServiceBean();
		}
		return user;
	}

	public boolean removeUser(UserServiceBean user) {
		logger.info("Initialized method UserManager.removeUser()");
		try {
			UserEntity ue = converter.serviceToEntity(user);
			userRepository.remove(ue);
			logger.info("Transaction completed");
		} catch (Exception e) {
			logger.error("Transaction failed "+e.getClass().getSimpleName());
			return false;
		}
		return true;
	}

	public boolean matches(UserServiceBean user) {
		logger.info("Initialized method UserManager.matches()");
		UserEntity dbUser = new UserEntity();
		try {
			dbUser = userRepository.findByUsername(user.getUsername());
			logger.info("Transaction completed");
			logger.info("Retrieved database user "+dbUser);
			logger.info("Checking credentials");
			return dbUser != null && HashUtils.matches(user.getPassword(), dbUser.getPassword());
		} catch (Exception e) {
			logger.error("Transaction failed "+e.getClass().getSimpleName());
			return false;
		}
	}
}
