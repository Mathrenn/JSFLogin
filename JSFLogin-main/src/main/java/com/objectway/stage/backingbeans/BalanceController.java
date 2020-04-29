package com.objectway.stage.backingbeans;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

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
	private static final ResourceBundle messages = ResourceBundle.getBundle("messages/messages");

	private Date selectedDate;

	@ManagedProperty("#{accountService}")
	private AccountService accountService;
	@ManagedProperty("#{clientController.selectedAccountBean}")
	private AccountViewBean selectedAccountBean;

	private BigDecimal balance;
	private BigDecimal meanBalance;
	private int deposits;
	private int withdrawals;

	public BalanceController() {
		super();
		selectedDate = new Date();
		balance = new BigDecimal(0.0);
		meanBalance = new BigDecimal(0.0);
		deposits = 0;
		withdrawals = 0;
	}

	// Getters and setters
	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public Date getMinDate() {
		return Date.from(selectedAccountBean.getDateIns().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
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
		LocalDate selectedLocalDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if(!selectedLocalDate.isBefore(selectedAccountBean.getDateIns()))
		{
			setBalance(getUpdatedBalance(selectedLocalDate));
			setDeposits(getUpdatedDeposits(selectedLocalDate));
			setWithdrawals(getUpdatedWithdrawals(selectedLocalDate));
			setMeanBalance(getUpdatedMeanBalance(selectedLocalDate));
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					messages.getString("balance.update.successful.message"),
					messages.getString("balance.update.successful.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					messages.getString("balance.update.failed.message"),
					messages.getString("balance.update.failed.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public BigDecimal getUpdatedBalance(LocalDate date) {
		logger.info("Started BalanceController.getUpdatedBalance()");
		return accountService.getBalanceToDate(
				accountViewConverter.viewToService(selectedAccountBean), 
				date).setScale(2);
	}

	public BigDecimal getUpdatedMeanBalance(LocalDate date) {
		logger.info("Started BalanceController.getUpdatedMeanBalance()");
		return accountService.getMeanBalanceToDate(
				accountViewConverter.viewToService(selectedAccountBean), 
				date).setScale(2);
	}

	public Integer getUpdatedDeposits(LocalDate date) {
		logger.info("Started BalanceController.getUpdatedDeposits()");
		return accountService.getDepositCountToDate(accountViewConverter.viewToService(selectedAccountBean), date);
	}

	public Integer getUpdatedWithdrawals(LocalDate date) {
		logger.info("Started BalanceController.getUpdatedWithdrawals()");
		return accountService.getWithdrawalCountToDate(accountViewConverter.viewToService(selectedAccountBean), date);
	}

	public String homeButton() {
		selectedDate = new Date();
		balance = new BigDecimal(0.0);
		deposits = 0;
		withdrawals = 0;
		meanBalance = new BigDecimal(0.0);
		return "/secured/welcome?faces-redirect=true";
	}

	public String backButton() {
		selectedDate = new Date();
		balance = new BigDecimal(0.0);
		deposits = 0;
		withdrawals = 0;
		meanBalance = new BigDecimal(0.0);
		return "/secured/client?faces-redirect=true";
	}
}
