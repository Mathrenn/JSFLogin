package com.objectway.stage.converter;

import java.util.ArrayList;
import java.util.List;

import com.objectway.stage.entity.AccountEntity;
import com.objectway.stage.entity.TransactionEntity;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.TransactionServiceBean;

public class TransactionEntityConverter implements ServiceEntityConverter<TransactionServiceBean, TransactionEntity>{
	private ServiceEntityConverter<AccountServiceBean, AccountEntity> contoConverter = new AccountEntityConverter();

	@Override
	public TransactionEntity serviceToEntity(TransactionServiceBean ts) {
		TransactionEntity te = new TransactionEntity();
		te.setAccount(contoConverter.serviceToEntity(ts.getAccount()));
		te.setDeposit(ts.isDeposit());
		te.setAmount(ts.getAmount());
		te.setDateIns(ts.getDateIns());
		return te;
	}
	
	@Override
	public TransactionServiceBean entityToService(TransactionEntity te) {
		TransactionServiceBean ts = new TransactionServiceBean();
		ts.setId(te.getId());
		ts.setAccount(contoConverter.entityToService(te.getAccount()));
		ts.setDeposit(te.isDeposit());
		ts.setAmount(te.getAmount());
		ts.setDateIns(te.getDateIns());
		return ts;
	}
	
	@Override
	public List<TransactionServiceBean> entityListToServiceList(List<TransactionEntity> tel) {
		List<TransactionServiceBean> tsl = new ArrayList<TransactionServiceBean>();
		tel.forEach(mdb -> tsl.add(entityToService(mdb)));
		return tsl;
	}

	@Override
	public List<TransactionEntity> serviceListToEntityList(List<TransactionServiceBean> tsl) {
		List<TransactionEntity> tel = new ArrayList<TransactionEntity>();
		tsl.forEach(ms -> tel.add(serviceToEntity(ms)));
		return tel;
	}
}
