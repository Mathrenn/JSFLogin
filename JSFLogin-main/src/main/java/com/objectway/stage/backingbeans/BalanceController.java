package com.objectway.stage.backingbeans;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objectway.stage.converter.AccountViewConverter;
import com.objectway.stage.converter.ServiceViewConverter;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.service.AccountService;
import com.objectway.stage.viewbeans.AccountViewBean;

@ManagedBean
@RequestScoped
public class BalanceController {
	private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
	private static final ServiceViewConverter<AccountServiceBean, AccountViewBean> accountViewConverter = new AccountViewConverter();

	private Date selectedDate;

	@ManagedProperty("#{accountService}")
	private AccountService accountService;
	@ManagedProperty("#{clientController.selectedAccountBean}")
	private AccountViewBean selectedAccountBean;

	private BigDecimal balance;
	private int deposits;
	private int withdrawals;
	private BigDecimal meanBalance;
	
	public BalanceController() {
		super();
		selectedDate = new Date();
		balance = new BigDecimal(0.0);
		deposits = 0;
		withdrawals = 0;
		meanBalance = new BigDecimal(0.0);;
	}

	// Getters and setters
	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	public AccountService getAccountService() {
		return accountService;
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	public AccountViewBean getSelectedAccountBean() {
		return selectedAccountBean;
	}
	
	public void setSelectedAccountBean(AccountViewBean selectedAccountBean) {
		this.selectedAccountBean = selectedAccountBean;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public int getDeposits() {
		return deposits;
	}

	public void setDeposits(int deposits) {
		this.deposits = deposits;
	}

	public int getWithdrawals() {
		return withdrawals;
	}

	public void setWithdrawals(int withdrawals) {
		this.withdrawals = withdrawals;
	}

	public BigDecimal getMeanBalance() {
		return meanBalance;
	}

	public void setMeanBalance(BigDecimal meanBalance) {
		this.meanBalance = meanBalance;
	}
	// End of getters and setters

	public void updateValuesToDate() {
		logger.info("Started BalanceController.updateValuesToDate()");
		setBalance(getUpdatedBalance(selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
		setDeposits(0);
		setWithdrawals(0);
		setMeanBalance(new BigDecimal(0.0));
	}
	
	public BigDecimal getUpdatedBalance(LocalDate date) {
		logger.info("Started BalanceController.getUpdatedBalance()");
		return accountService.getBalanceToDate(
				accountViewConverter.viewToService(selectedAccountBean), 
				date).setScale(2);
	}

	public BigDecimal getUpdatedMeanBalance(LocalDate date) {
		logger.info("Started BalanceController.getUpdatedMeanBalance()");
		return accountService.getBalanceToDate(
				accountViewConverter.viewToService(selectedAccountBean), 
				date).setScale(2);
	}

	public Integer getUpdatedDeposits(LocalDate date) {
		logger.info("Started BalanceController.getUpdatedDeposits()");
		return 0;
	}

	public Integer getUpdatedWithdrawals(LocalDate date) {
		logger.info("Started BalanceController.getUpdatedDeposits()");
		return 0;
	}

	public String homeButton() {
		selectedDate = new Date();
		return "/secured/welcome?faces-redirect=true";
	}

	public String backButton() {
		selectedDate = new Date();
		return "/secured/client?faces-redirect=true";
	}
}
