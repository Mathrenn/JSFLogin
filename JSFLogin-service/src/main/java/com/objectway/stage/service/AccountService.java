package com.objectway.stage.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objectway.stage.manager.AccountManager;
import com.objectway.stage.manager.TransactionsManager;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.ClientServiceBean;
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
	
	public AccountServiceBean addDefaultAccount(ClientServiceBean client, LocalDate date) {
		return accountManager.addDefault(client, date);
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
		return getBalanceToDate(
				transactionsManager.findByAccountAndDate(account, date),
				date);
	}

	public BigDecimal getBalanceToDate(List<TransactionServiceBean> transactions, LocalDate date) {
		BigDecimal balance = new BigDecimal(0.0);
		for(TransactionServiceBean m: transactions) {
			if(!m.getDateIns().isAfter(date)) {
				if(m.isDeposit()) {
					balance = balance.add(m.getAmount());
				} else {
					balance = balance.subtract(m.getAmount());
				}
			}
		}
		return balance;
	}
	
	public BigDecimal getMeanBalanceToDate(AccountServiceBean account, LocalDate date) {
		return getMeanBalanceToDate(
				transactionsManager.findByAccountAndDate(account, date),
				date);
	}

	public BigDecimal getMeanBalanceToDate(AccountServiceBean account, LocalDate from, LocalDate to) {
		if(!from.isBefore(account.getDateIns()) && !to.isBefore(account.getDateIns())) {
			if(from.isBefore(to)) {
				return getMeanBalanceToDate(
						transactionsManager.findByAccountAndDate(account, to),
						from,
						to);
			} else {
				return getMeanBalanceToDate(
						transactionsManager.findByAccountAndDate(account, from),
						to,
						from);
			}
		} else {
			return new BigDecimal(0.0).setScale(2);
		}
	}

	private BigDecimal getMeanBalanceToDate(List<TransactionServiceBean> transactions, LocalDate date) {
		BigDecimal saldo = new BigDecimal(0.0).setScale(2);

		if(!transactions.isEmpty()) {
			// get list of different days
			// up to the given date
			List<LocalDate> days = transactions
					.stream()
					.map(t -> t.getDateIns())
					.filter(d -> !d.isAfter(date))
					.distinct()
					.collect(Collectors.toList());

			if(!days.isEmpty()) {
				for(LocalDate day: days) {
					saldo = saldo.add(getBalanceToDate(transactions, day));
				}

				return saldo.divide(new BigDecimal(days.size())).setScale(2);
			}
		}
		return saldo;
	}

	private BigDecimal getMeanBalanceToDate(List<TransactionServiceBean> transactions, LocalDate from, LocalDate to) {
		BigDecimal saldo = new BigDecimal(0.0).setScale(2);

		if(!transactions.isEmpty()) {
			// get list of different days
			// up to the given date
			List<LocalDate> days = new ArrayList<>();
			if(from.isBefore(to)) {
				days = transactions
						.stream()
						.map(t -> t.getDateIns())
						.filter(d -> !d.isBefore(from) && !d.isAfter(to))
						.distinct()
						.collect(Collectors.toList());
			} else {
				days = transactions
						.stream()
						.map(t -> t.getDateIns())
						.filter(d -> !d.isBefore(to) && !d.isAfter(from))
						.distinct()
						.collect(Collectors.toList());
			}

			if(!days.isEmpty()) {
				for(LocalDate day: days) {
					saldo = saldo.add(getBalanceToDate(transactions, day));
				}
				return saldo.divide(new BigDecimal(days.size()), 2, RoundingMode.HALF_EVEN);
			}
		}
		return saldo;
	}
	
	public Integer getDepositCountToDate(AccountServiceBean account, LocalDate date) {
		return (int) transactionsManager.findByAccountAndDate(account, date).stream().collect(Collectors.toSet())
				.stream()
				.filter(t -> t.isDeposit() && !t.getDateIns().isAfter(date))
				.count();
	}

	public Integer getWithdrawalCountToDate(AccountServiceBean account, LocalDate date) {
		return (int) transactionsManager.findByAccountAndDate(account, date).stream().collect(Collectors.toSet())
				.stream()
				.filter(t -> !t.isDeposit() && !t.getDateIns().isAfter(date))
				.count();
	}

	public Integer getDepositCountBetweenDates(AccountServiceBean account, LocalDate from, LocalDate to) {
		if(!from.isBefore(account.getDateIns()) && !to.isBefore(account.getDateIns())) {
			if(from.isBefore(to)) {
				return (int) transactionsManager.findByAccountAndDate(account, to).stream().collect(Collectors.toSet())
						.stream()
						.filter(t -> t.isDeposit() && !t.getDateIns().isBefore(from) && !t.getDateIns().isAfter(to))
						.count();
			} else {
				return (int) transactionsManager.findByAccountAndDate(account, to).stream().collect(Collectors.toSet())
						.stream()
						.filter(t -> t.isDeposit() && !t.getDateIns().isBefore(to) && !t.getDateIns().isAfter(from))
						.count();
				
			}
		} else {
			return 0;
		}
	}

	public Integer getWithdrawalCountBetweenDates(AccountServiceBean account, LocalDate from, LocalDate to) {
		if(!from.isBefore(account.getDateIns()) && !to.isBefore(account.getDateIns())) {
			if(from.isBefore(to)) {
			return (int) transactionsManager.findByAccountAndDate(account, to).stream().collect(Collectors.toSet())
					.stream()
					.filter(t -> !t.isDeposit() && !t.getDateIns().isBefore(from) && !t.getDateIns().isAfter(to))
					.count();
			} else {
				return (int) transactionsManager.findByAccountAndDate(account, to).stream().collect(Collectors.toSet())
						.stream()
						.filter(t -> !t.isDeposit() && !t.getDateIns().isBefore(to) && !t.getDateIns().isAfter(from))
						.count();
			}
		} else {
			return 0;
		}
	}

	public void orderDates(LocalDate from, LocalDate to) {
		if(from.isAfter(to)) {
			LocalDate temp = from;
			from = to;
			to = temp;
		}
	}
	
}
