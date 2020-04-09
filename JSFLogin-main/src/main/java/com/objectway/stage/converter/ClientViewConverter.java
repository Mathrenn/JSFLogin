package com.objectway.stage.converter;

import java.util.ArrayList;
import java.util.List;

import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.model.UserServiceBean;
import com.objectway.stage.viewbeans.ClientViewBean;
import com.objectway.stage.viewbeans.UserViewBean;

public class ClientViewConverter implements ServiceViewConverter<ClientServiceBean, ClientViewBean>{
	private ServiceViewConverter<UserServiceBean, UserViewBean> userViewConverter = new UserViewConverter();
	
	@Override
	public ClientServiceBean viewToService(ClientViewBean cv) {
		ClientServiceBean cs = new ClientServiceBean();
		cs.setName(cv.getName());
		cs.setSurname(cv.getSurname());
		cs.setCodiceFiscale(cv.getCodiceFiscale());
		cs.setUser(userViewConverter.viewToService(cv.getUserBean()));
		return cs;
	}
	
	@Override
	public List<ClientServiceBean> viewListToServiceList(List<ClientViewBean> cvl){
		List<ClientServiceBean> csl = new ArrayList<>();
		cvl.forEach(cv -> csl.add(viewToService(cv)));
		return csl;
	}

	@Override
	public ClientViewBean serviceToView(ClientServiceBean cs) {
		ClientViewBean cv = new ClientViewBean();
		cv.setName(cs.getName());
		cv.setSurname(cs.getSurname());
		cv.setCodiceFiscale(cs.getCodiceFiscale());
		cv.setUserBean(userViewConverter.serviceToView(cs.getUser()));
		return cv;
	}

	@Override
	public List<ClientViewBean> serviceListToViewList(List<ClientServiceBean> csl){
		List<ClientViewBean> cvl = new ArrayList<>();
		csl.forEach(cs -> cvl.add(serviceToView(cs)));
		return cvl;
	}
}
