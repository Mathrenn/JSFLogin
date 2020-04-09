package com.objectway.stage.converter;

import java.util.ArrayList;
import java.util.List;

import com.objectway.stage.entity.ClientEntity;
import com.objectway.stage.entity.AccountEntity;
import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.model.AccountServiceBean;

public class AccountEntityConverter implements ServiceEntityConverter<AccountServiceBean, AccountEntity>{
	private static final ServiceEntityConverter<ClientServiceBean, ClientEntity> clientConverter = new ClientEntityConverter();
	
	@Override
	public AccountServiceBean entityToService(AccountEntity ae) {
		if(ae == null)
			return null;
		AccountServiceBean as =  new AccountServiceBean();
		as.setId(ae.getId());
		as.setClient(clientConverter.entityToService(ae.getClient()));
		as.setBalance(ae.getBalance());
		as.setDateIns(ae.getDateIns());
		return as;
	}
	
	@Override
	public List<AccountServiceBean> entityListToServiceList(List<AccountEntity> ael){
		if(ael == null)
			return null;		
		List<AccountServiceBean> asl = new ArrayList<>();
		ael.stream().forEach(cce -> asl.add(entityToService(cce)));
		return asl;
	}
	
	@Override
	public AccountEntity serviceToEntity(AccountServiceBean as) {
		if(as == null)
			return null;
		AccountEntity ae =  new AccountEntity();
		ae.setBalance(as.getBalance());
		ae.setDateIns(as.getDateIns());
		ae.setClient(clientConverter.serviceToEntity(as.getClient()));
		return ae;
	}

	@Override
	public List<AccountEntity> serviceListToEntityList(List<AccountServiceBean> asl){
		if(asl == null)
			return null;
		List<AccountEntity> ael = new ArrayList<>();
		asl.stream().forEach(ccs -> ael.add(serviceToEntity(ccs)));
		return ael;
	}
	
	public List<AccountEntity> entityIterToList(Iterable<AccountEntity> aei){
		if(aei == null)
			return null;
		List<AccountEntity> ael = new ArrayList<>();
		aei.forEach(cce -> ael.add(cce));
		return ael;
	}
}
