package com.objectway.stage.backingbeans;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objectway.stage.converter.AccountViewConverter;
import com.objectway.stage.converter.ClientViewConverter;
import com.objectway.stage.converter.ServiceViewConverter;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.service.AccountService;
import com.objectway.stage.viewbeans.AccountViewBean;
import com.objectway.stage.viewbeans.ClientViewBean;

@ManagedBean
@SessionScoped
public class ClientController {
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	private static final ResourceBundle messages = ResourceBundle.getBundle("messages/messages");
	private static final ServiceViewConverter<ClientServiceBean, ClientViewBean> clientViewConverter = new ClientViewConverter();
	private static final ServiceViewConverter<AccountServiceBean, AccountViewBean> accountViewConverter = new AccountViewConverter();

	private AccountViewBean selectedAccountBean;
	private List<AccountViewBean> accounts;
	
	@ManagedProperty("#{accountService}")
	private AccountService accountService;
	@ManagedProperty("#{welcomeController}")
	private WelcomeController welcomeController;

	public ClientController() {
		super();
		selectedAccountBean = new AccountViewBean();
		accounts = new ArrayList<>();  
	}

	// getters and setters
	public AccountViewBean getSelectedAccountBean() {
		return selectedAccountBean;
	}

	public void setSelectedAccountBean(AccountViewBean selectedAccountBean) {
		this.selectedAccountBean = selectedAccountBean;
	}

	public List<AccountViewBean> getAccounts() {
		if (FacesContext.getCurrentInstance().getRenderResponse()) {
			updateAccounts();
		}
		return accounts;
	}

	public void setAccounts(List<AccountViewBean> accounts) {
		this.accounts = accounts;
	}
	
	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	// end of getters and setters
	public WelcomeController getWelcomeController() {
		return welcomeController;
	}

	public void setWelcomeController(WelcomeController welcomeController) {
		this.welcomeController = welcomeController;
	}

	public ClientViewBean getSelectedClientBean() {
		return welcomeController.getSelectedClientBean();
	}

	public void setSelectedClientBean(ClientViewBean selectedClientBean) {
		welcomeController.setSelectedClientBean(selectedClientBean);
	}

	public String openDefaultAccount() {
		logger.info("Started ClientController.openDefaultAccount()");
		AccountViewBean conto = accountViewConverter.serviceToView(
				accountService.addDefaultAccount(clientViewConverter.viewToService(getSelectedClientBean())));
		if(conto != null && conto.getClient().equals(getSelectedClientBean())) {
			updateAccounts();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					messages.getString("account.opensuccessful.message"),
					messages.getString("account.opensuccessful.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			logger.info("Failed to add conto");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					messages.getString("account.openfailed.message"),
					messages.getString("account.openfailed.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return "/secured/client";
	}

	public void updateAccounts() {
		logger.info("Started ClientController.updateAccounts()");
		setSelectedAccountBean(new AccountViewBean());
		if(getSelectedClientBean().getCodiceFiscale()!=null && !getSelectedClientBean().getCodiceFiscale().equals("")) {
			logger.info("Searching accounts for client: "+getSelectedClientBean().getName());
			setAccounts(accountViewConverter.serviceListToViewList(
					accountService.findByClient(
							clientViewConverter.viewToService(getSelectedClientBean()))));
			for(AccountViewBean c: accounts) {
				c.setBalance(accountService.getBalanceToDate(accountViewConverter.viewToService(c), LocalDate.MAX));
			}
			logger.info("Retrieved "+accounts.size()+" accounts");
		} else {
			logger.debug("Invalid client\n"+getSelectedClientBean());
		}
	}
	
	public String homeButton() {
		selectedAccountBean = new AccountViewBean();
		return "/secured/welcome?faces-redirect=true";
	}
	
	public String backButton() {
		selectedAccountBean = new AccountViewBean();
		return "/secured/welcome?faces-redirect=true";
	}
	
	// Listeners
	public void onAccountSelect(SelectEvent event) {
		logger.info("Started ClientController.onAccountSelect()");
		selectedAccountBean = (AccountViewBean) event.getObject();
		selectedAccountBean.setClient(getSelectedClientBean());
		String page = "";
		if(isValidAccount(selectedAccountBean)) {
			logger.debug("Account is valid\n" + getSelectedClientBean());
			page = "/secured/transactions?faces-redirect=true";
		} else {
			logger.debug("Account is not valid\n" + getSelectedClientBean());
			page = "/secured/client";
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.getApplication().getNavigationHandler().handleNavigation(context, null, page);
	}
	// End of listeners

	// Validators
	public boolean isValidAccount(AccountViewBean c) {
		if(c != null && 
		   c.getBalance()!=null && 
		   c.getDateIns()!=null) {
			return true;
		}
		return false;
	}
	// End of validators
}
