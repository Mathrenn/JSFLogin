package com.objectway.stage.converter;

import java.util.ArrayList;
import java.util.List;

import com.objectway.stage.entity.ClientEntity;
import com.objectway.stage.entity.UserEntity;
import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.model.UserServiceBean;

public class ClientEntityConverter implements ServiceEntityConverter<ClientServiceBean, ClientEntity>{
	private static final ServiceEntityConverter<UserServiceBean, UserEntity> userConverter = new UserEntityConverter();
	
	@Override
	public ClientEntity serviceToEntity(ClientServiceBean csb) {
		ClientEntity ce = new ClientEntity(csb.getName(), 
										   csb.getSurname(), 
										   csb.getCodiceFiscale(), 
										   userConverter.serviceToEntity(csb.getUser()));
		return ce;
	}

	@Override
	public ClientServiceBean entityToService(ClientEntity ce) {
		ClientServiceBean csb = new ClientServiceBean(ce.getName(), 
									   ce.getSurname(), 
									   ce.getCodiceFiscale(), 
									   userConverter.entityToService(ce.getUser()));
		csb.setId(ce.getId());
		return csb;
	}

	public List<ClientEntity> entityIterToList(Iterable<ClientEntity> cei){
		List<ClientEntity> cel = new ArrayList<ClientEntity>();
		cei.forEach(ce -> cel.add(ce));
		return cel;
	}

	@Override
	public List<ClientServiceBean> entityListToServiceList(List<ClientEntity> cei){
		List<ClientServiceBean> csbl = new ArrayList<ClientServiceBean>();
		cei.forEach(ce -> csbl.add(entityToService(ce)));
		return csbl;
	}

	@Override
	public List<ClientEntity> serviceListToEntityList(List<ClientServiceBean> csbl) {
		List<ClientEntity> cel = new ArrayList<ClientEntity>();
		csbl.forEach(csb -> cel.add(serviceToEntity(csb)));
		return cel;
	}
}
