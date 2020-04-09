package com.objectway.stage.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objectway.stage.manager.AccountManager;
import com.objectway.stage.manager.TransactionsManager;
import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.TransactionServiceBean;

@Component
public class AccountService{
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private TransactionsManager transactionsManager;

	//getters and setters
	public AccountManager getAccountManager() {
		return accountManager;
	}
	
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
	public TransactionsManager getTransactionsManager() {
		return transactionsManager;
	}
	
	public void setTransactionsManager(TransactionsManager transactionsManager) {
		this.transactionsManager = transactionsManager;
	}
	//end of getters and setters

	public List<TransactionServiceBean> findAllTransactionsByAccount(AccountServiceBean account) {
		return transactionsManager.findByAccount(account);
	}

	public List<AccountServiceBean> findByClient(ClientServiceBean cliente) {
		return accountManager.findByClient(cliente);
	}
	
	public AccountServiceBean findById(Long id) {
		return accountManager.findById(id);
	}
	
	public AccountServiceBean addDefaultAccount(ClientServiceBean client) {
		return accountManager.addDefault(client);
	}

	public AccountServiceBean addTransaction(TransactionServiceBean transaction) {
		AccountServiceBean account = new AccountServiceBean();
		if(transaction != null) {
			TransactionServiceBean t = transactionsManager.add(transaction);
			account = t.getAccount();
		}
		return account;
	}

	public BigDecimal getBalanceToDate(AccountServiceBean account, LocalDate date) {
		AccountServiceBean a = accountManager.findById(account.getId());
		a.setTransactionList(transactionsManager.findByAccountAndDate(account, date).stream().collect(Collectors.toSet()));
		BigDecimal balance = new BigDecimal(0.0);
		balance = a.getBalanceToDate(date);
		return balance;
	}
}
