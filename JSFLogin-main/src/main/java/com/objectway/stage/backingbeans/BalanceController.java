package com.objectway.stage.backingbeans;

import java.math.BigDecimal;
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

	public BalanceController() {
		super();
		selectedDate = new Date();
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
	// End of getters and setters

	// getter fittizio, usato in balance.xhtml
	public BigDecimal getBalanceToDate() {
		logger.info("Started BalanceController.getSaldoToDate()");
		return accountService.getBalanceToDate(
				accountViewConverter.viewToService(selectedAccountBean), 
				selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).setScale(2);
	}
	// setter fittizio
	public void setBalanceToDate() {}

	public String homeButton() {
		selectedDate = new Date();
		return "/secured/welcome?faces-redirect=true";
	}

	public String backButton() {
		selectedDate = new Date();
		return "/secured/client?faces-redirect=true";
	}
}
