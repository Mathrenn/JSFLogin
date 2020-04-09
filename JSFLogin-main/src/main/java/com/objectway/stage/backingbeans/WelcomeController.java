package com.objectway.stage.backingbeans;

import java.text.MessageFormat;
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

import com.objectway.stage.converter.ClientViewConverter;
import com.objectway.stage.converter.ServiceViewConverter;
import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.service.ClientService;
import com.objectway.stage.viewbeans.ClientViewBean;
import com.objectway.stage.viewbeans.UserViewBean;

@ManagedBean
@SessionScoped
public class WelcomeController {
	private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
	private static final ResourceBundle messages = ResourceBundle.getBundle("messages/messages");
	private static final ServiceViewConverter<ClientServiceBean, ClientViewBean> clientViewConverter = new ClientViewConverter();

	private ClientViewBean clientBean;
	private ClientViewBean selectedClientBean;
	private List<ClientViewBean> clients;

	@ManagedProperty("#{clientService}")
	private ClientService clientService;
	@ManagedProperty("#{loginController}")
	private LoginController loginController;

	public WelcomeController() {
		super();
		clientBean = new ClientViewBean();
		selectedClientBean = new ClientViewBean();
		clients = new ArrayList<>();
	}

	// getters and setters
	public ClientService getClientService() {
		return clientService;
	}
	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}
	public ClientViewBean getClientBean() {
		return clientBean;
	}
	public void setClientBean(ClientViewBean clientBean) {
		this.clientBean = clientBean;
	}
	public ClientViewBean getSelectedClientBean() {
		return selectedClientBean;
	}
	public void setSelectedClientBean(ClientViewBean selectedClientBean) {
		this.selectedClientBean = selectedClientBean;
	}
	public List<ClientViewBean> getClients() {
		if (FacesContext.getCurrentInstance().getRenderResponse()) {
			updateClients();
		}
		return clients;
	}
	public void setClients(List<ClientViewBean> clients) {
		this.clients = clients;
	}
	
	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public UserViewBean getUserBean() {
		return loginController.getUserBean();
	}
	public void setUserBean(UserViewBean userBean) {
		loginController.setUserBean(userBean);
	}
	// end of getters and setters

	public void updateClients() {
		logger.info("Started WelcomeController.updateClients()");
		setSelectedClientBean(new ClientViewBean());
		if(clientBean.getUserBean().getUsername()!=null) {
			logger.info("Searching clients for user: "+getUserBean().getUsername());
			setClients(clientViewConverter.serviceListToViewList(
					clientService.findByUserName(getUserBean().getUsername())));
			logger.info("retrieved "+clients.size()+" clients");
		} else {
			logger.debug("User name is null");
		}
	}

	public String registerClient() {
		logger.info("Started WelcomeController.registerClient()");
		if(isValidCodice(clientBean.getCodiceFiscale())) {
			clientBean.setUserBean(getUserBean());
			ClientViewBean client = clientViewConverter.serviceToView(
					clientService.register(
							clientViewConverter.viewToService(clientBean)));
			if(client.equals(clientBean)) {
				clients.add(client);
				updateClients();
				clientBean = new ClientViewBean();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
						MessageFormat.format(messages.getString("client.added.successful.message"), getUserBean().getUsername()),
						messages.getString("client.added.successful.description"));
				FacesContext.getCurrentInstance().addMessage(null, message);
			} else {
				logger.debug("ClientController registration failed");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						messages.getString("client.added.failed.message"),
						messages.getString("client.added.failed.description"));
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} else {
			logger.debug("Invalid codice");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					messages.getString("codice.invalid.message"),
					messages.getString("codice.invalid.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return "/secured/welcome";
	}
	public String homeButton() {
		clientBean = new ClientViewBean();
		selectedClientBean = new ClientViewBean();
		return "/secured/welcome?faces-redirect=true";
	}

	// Listeners
	public void onClientSelect(SelectEvent event) {
		logger.info("Started WelcomeController.onClientSelect()");
		selectedClientBean = (ClientViewBean) event.getObject();
		String page = "";
		if(isValidClient(selectedClientBean)) {
			logger.debug("client is valid\n" + selectedClientBean);
			page = "/secured/client?faces-redirect=true";
		} else {
			logger.debug("client is not valid\n" + selectedClientBean);
			page = "/secured/welcome";
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.getApplication().getNavigationHandler().handleNavigation(context, null, page);
	}
	
	//validators
	public boolean isValidCodice(String codiceFiscale) {
		logger.debug("Checking codiceFiscale");
		if(codiceFiscale!=null && 
				codiceFiscale.toLowerCase()
				.matches("[a-z]{6}"
						+ "\\d{2}"
						+ "[a-z]\\d{2}"
						+ "[a-z]\\d{3}[a-z]")) {
			return true;
		}
		return false;
	}

	public boolean isValidClient(ClientViewBean c) {
		if(c != null && 
				c.getName()!="" && 
				c.getSurname()!="" && 
				isValidCodice(c.getCodiceFiscale())) {
			return true;
		}
		return false;
	}
}
