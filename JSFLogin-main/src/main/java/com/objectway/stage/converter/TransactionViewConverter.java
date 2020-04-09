package com.objectway.stage.converter;

import java.util.ArrayList;
import java.util.List;

import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.TransactionServiceBean;
import com.objectway.stage.viewbeans.AccountViewBean;
import com.objectway.stage.viewbeans.TransactionViewBean;

public class TransactionViewConverter implements ServiceViewConverter<TransactionServiceBean, TransactionViewBean>{
	private static final ServiceViewConverter<AccountServiceBean, AccountViewBean> contoCorrenteViewConverter = new AccountViewConverter();
	
	@Override
	public TransactionServiceBean viewToService(TransactionViewBean tv) {
		TransactionServiceBean ts = new TransactionServiceBean();
		ts.setAmount(tv.getAmount());
		ts.setDeposit(tv.isDeposit());
		ts.setDateIns(tv.getDateIns());
		ts.setAccount(contoCorrenteViewConverter.viewToService(tv.getAccount()));
		return ts;
	}

	@Override
	public TransactionViewBean serviceToView(TransactionServiceBean ts) {
		TransactionViewBean tv = new TransactionViewBean();
		tv.setAmount(ts.getAmount());
		tv.setDeposit(ts.isDeposit());
		tv.setDateIns(ts.getDateIns());
		tv.setAccount(contoCorrenteViewConverter.serviceToView(ts.getAccount()));
		return tv;
	}

	@Override
	public List<TransactionServiceBean> viewListToServiceList(List<TransactionViewBean> tvl) {
		List<TransactionServiceBean> tsl = new ArrayList<>();
		tvl.forEach(tv -> tsl.add(viewToService(tv)));
		return tsl;
	}

	@Override
	public List<TransactionViewBean> serviceListToViewList(List<TransactionServiceBean> tsl) {
		List<TransactionViewBean> tvl = new ArrayList<>();
		tsl.forEach(ts -> tvl.add(serviceToView(ts)));
		return tvl;
	}
}
