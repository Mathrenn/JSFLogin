package com.objectway.stage.backingbeans;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objectway.stage.converter.AccountViewConverter;
import com.objectway.stage.converter.ServiceViewConverter;
import com.objectway.stage.converter.TransactionViewConverter;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.TransactionServiceBean;
import com.objectway.stage.service.AccountService;
import com.objectway.stage.viewbeans.AccountViewBean;
import com.objectway.stage.viewbeans.TransactionViewBean;

@ManagedBean
@ViewScoped
public class TransactionsController {
	private static final Logger logger = LoggerFactory.getLogger(TransactionsController.class);
	private static final ResourceBundle messages = ResourceBundle.getBundle("messages/messages");
	private static final ServiceViewConverter<TransactionServiceBean, TransactionViewBean> transactionViewConverter = new TransactionViewConverter();
	private static final ServiceViewConverter<AccountServiceBean, AccountViewBean> accountViewConverter = new AccountViewConverter();

	private TransactionViewBean transactionBean;
	private List<TransactionViewBean> transactions;

	@ManagedProperty("#{accountService}")
	private AccountService accountService;
	@ManagedProperty("#{clientController.selectedAccountBean}")
	private AccountViewBean selectedAccountBean;

	public TransactionsController() {
		super();
		transactionBean = new TransactionViewBean();
		transactions = new ArrayList<>();
	}
	// getters and setters
	public TransactionViewBean getTransactionBean() {
		return transactionBean;
	}
	
	public void setTransactionBean(TransactionViewBean transactionBean) {
		this.transactionBean = transactionBean;
	}

	public Date getCurrentDate() {
		return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public void setCurrentDate(Date currentDate) {
		transactionBean.setDateIns(currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public List<TransactionViewBean> getTransactions() {
		if(transactions!=null) {
			updateTransactions();
		}
		return transactions;
	}

	public void setTransactions(List<TransactionViewBean> transactions) {
		this.transactions = transactions;
	}

	public AccountViewBean getSelectedAccountBean() {
		return selectedAccountBean;
	}

	public void setSelectedAccountBean(AccountViewBean selectedAccountBean) {
		this.selectedAccountBean = selectedAccountBean;
	}

	public AccountService getAccountService() {
		return accountService;
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	// end of getters and setters

	public void updateTransactions() {
		logger.info("Started TransactionsController.updateTransactions()");
		logger.info("Searching transactions for the selected account");
		setTransactions(transactionViewConverter.serviceListToViewList(
				accountService.findAllTransactionsByAccount(
						accountViewConverter.viewToService(selectedAccountBean))));
		logger.info("retrieved "+transactions.size()+" transactions");
	}

	public String addTransaction() {
		logger.info("Started TransactionsController.addMovement()");
		transactionBean.setAccount(selectedAccountBean);
		AccountViewBean conto = accountViewConverter.serviceToView(
				accountService.addTransaction(transactionViewConverter.viewToService(transactionBean)));
		if(conto != null && conto.getClient().equals(selectedAccountBean.getClient())) {
			updateTransactions();
			transactionBean = new TransactionViewBean();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					messages.getString("transaction.addsuccessful.message"),
					messages.getString("transaction.addsuccessful.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			logger.info("Failed to add conto");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					messages.getString("transaction.addfailed.message"),
					messages.getString("transaction.addfailed.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return "/secured/transactions";
	}
	
	public String homeButton() {
		return "/secured/welcome?faces-redirect=true";
	}

	public String backButton() {
		return "/secured/client?faces-redirect=true";
	}
}
