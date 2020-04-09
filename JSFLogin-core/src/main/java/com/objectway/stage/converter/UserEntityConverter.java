package com.objectway.stage.converter;

import java.util.ArrayList;
import java.util.List;

import com.objectway.stage.entity.UserEntity;
import com.objectway.stage.model.UserServiceBean;

public class UserEntityConverter implements ServiceEntityConverter<UserServiceBean, UserEntity>{
	@Override
	public UserEntity serviceToEntity(UserServiceBean ub) {
		UserEntity ue = new UserEntity();
		ue.setUsername(ub.getUsername());
		ue.setPassword(ub.getPassword());
		return ue;
	}

	@Override
	public UserServiceBean entityToService(UserEntity ue) {
		UserServiceBean ub = new UserServiceBean();
		ub.setId(ue.getId());
		ub.setUsername(ue.getUsername());
		ub.setPassword(ue.getPassword());
		return ub;
	}
	
	public List<UserEntity> entityIterToList(Iterable<UserEntity> uei){
		List<UserEntity> uel = new ArrayList<UserEntity>();
		uel.forEach(ue -> uel.add(ue));
		return uel;
	}
	
	@Override
	public List<UserServiceBean> entityListToServiceList(List<UserEntity> uel){
		List<UserServiceBean> usbl = new ArrayList<UserServiceBean>();
		uel.forEach(ue -> usbl.add(entityToService(ue)));
		return usbl;
	}

	@Override
	public List<UserEntity> serviceListToEntityList(List<UserServiceBean> usbl) {
		List<UserEntity> uel = new ArrayList<UserEntity>();
		usbl.forEach(usb -> uel.add(serviceToEntity(usb)));
		return uel;
	}
}
