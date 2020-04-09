package com.objectway.stage.converter;

import java.util.ArrayList;
import java.util.List;

import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.viewbeans.AccountViewBean;

public class AccountViewConverter implements ServiceViewConverter<AccountServiceBean, AccountViewBean>{
	private static final ClientViewConverter clientConverter = new ClientViewConverter();
	
	@Override
	public AccountServiceBean viewToService(AccountViewBean av) {
		AccountServiceBean as = new AccountServiceBean();
		as.setId(av.getId());
		as.setClient(clientConverter.viewToService(av.getClient()));
		as.setBalance(av.getBalance());
		as.setDateIns(av.getDateIns());
		return as;
	}
	
	@Override
	public AccountViewBean serviceToView(AccountServiceBean as) {
		AccountViewBean av = new AccountViewBean();
		av.setId(as.getId());
		av.setClient(clientConverter.serviceToView(as.getClient()));
		av.setBalance(as.getBalance());
		av.setDateIns(as.getDateIns());
		return av;
	}

	@Override
	public List<AccountServiceBean> viewListToServiceList(List<AccountViewBean> avl) {
		List<AccountServiceBean> asl = new ArrayList<>();
		avl.forEach(av -> asl.add(viewToService(av)));
		return asl;
	}

	@Override
	public List<AccountViewBean> serviceListToViewList(List<AccountServiceBean> asl) {
		List<AccountViewBean> avl = new ArrayList<>();
		asl.forEach(as -> avl.add(serviceToView(as)));
		return avl;
	}
}
