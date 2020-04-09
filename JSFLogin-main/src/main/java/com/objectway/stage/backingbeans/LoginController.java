package com.objectway.stage.backingbeans;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objectway.stage.converter.ServiceViewConverter;
import com.objectway.stage.converter.UserViewConverter;
import com.objectway.stage.model.UserServiceBean;
import com.objectway.stage.service.UserService;
import com.objectway.stage.viewbeans.UserViewBean;

@ManagedBean
@SessionScoped
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	private static final ResourceBundle messages = ResourceBundle.getBundle("messages/messages");
	private static final ServiceViewConverter<UserServiceBean, UserViewBean> userViewConverter = new UserViewConverter();

	private UserViewBean userBean;

	@ManagedProperty("#{userService}")
	private UserService userService;

	public LoginController() {
		super();
		userBean = new UserViewBean();
	}
	public LoginController(UserViewBean userBean) {
		super();
		this.userBean = userBean;
	}

	// getters and setters
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserViewBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserViewBean userBean) {
		this.userBean = userBean;
	}

	public String login() {
		logger.info("Started FrontController.login()");

		if(isValidPassword(userBean.getPassword())) {
			if(userService.login(userViewConverter.viewToService(userBean))) {
				userBean.setLoggedIn(true);
				return "/secured/welcome?faces-redirect=true";
			} else {
				logger.debug("Wrong user or password");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						messages.getString("login.invalid.message"),
						messages.getString("login.invalid.description"));
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} else {
			logger.debug("Invalid password");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					messages.getString("password.invalid.message"),
					messages.getString("password.invalid.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		userBean.setLoggedIn(false);
		return "/login";
	}

	public String registerUser() {
		logger.info("Started FrontController.registerUser()");
		if(isValidPassword(userBean.getPassword())) {
			if(userViewConverter.serviceToView(
					userService.register(userViewConverter.viewToService(userBean)))
					.equals(userBean)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
						MessageFormat.format(messages.getString("registration.successful.message"), userBean.getUsername()),
						messages.getString("registration.successful.description"));
				FacesContext.getCurrentInstance().addMessage(null, message);
			} else {
				logger.info("Registration failed");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						messages.getString("registration.failed.message"),
						messages.getString("registration.failed.description"));
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} else {
			logger.info("Invalid password");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					messages.getString("password.invalid.message"),
					messages.getString("password.invalid.description"));
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		userBean.setLoggedIn(false);
		return "/login";
	}

	public String logout() {
		userBean = new UserViewBean();
		return "/login?faces-redirect=true";
	}

	// listeners
	public void checkLogin(ComponentSystemEvent event) {
		logger.info("Checking if user is valid");
		if(!isValidUser(userBean)) {
			logger.debug("User is not valid");
			FacesContext context = FacesContext.getCurrentInstance();
			context.getApplication().getNavigationHandler()
				.handleNavigation(context, null, "/login?faces-redirect=true");
		} else {
			logger.debug("User is valid");
		}
	}

	// validators
	public boolean isValidUser(UserViewBean user) {
		if(userBean == null 
				|| !userBean.isLoggedIn() 
				|| userBean.getUsername().equals("") 
				|| !isValidPassword(user.getPassword())) {
			return false;
		}
		return true;
	}

	public boolean isValidPassword(String pass) {
		logger.debug("Checking password");
		if(pass!=null && 
				pass.matches("((?=.*[a-z])"
						+ "(?=.*\\d)"
						+ "(?=.*[A-Z])"
						+ "(?=.*[@#$%!?_-]).{8,})")) {
			return true;
		}
		return false;
	}
}
