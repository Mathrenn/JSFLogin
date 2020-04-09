package com.objectway.stage.viewbeans;

public class ClientViewBean {
	private String name;
	private String surname;
	private String codiceFiscale;
	private UserViewBean userBean;

	public ClientViewBean() {
		super();
		this.name = "";
		this.surname = "";
		this.codiceFiscale = "";
		this.userBean = new UserViewBean();
	}
	
	public ClientViewBean(String name, String surname, String codiceFiscale, UserViewBean userBean) {
		super();
		this.name = name;
		this.surname = surname;
		this.codiceFiscale = codiceFiscale;
		this.userBean = userBean;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name!=null && !name.equals("")) {
			this.name = name.substring(0, 1).toUpperCase().concat(
					name.substring(1).toLowerCase());
		} else {
			this.name = "";
		}
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		if(surname!=null && !surname.equals("")) {
			this.surname = surname.substring(0, 1).toUpperCase().concat(
					surname.substring(1).toLowerCase());
		} else {
			this.surname = "";
		}
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		if(codiceFiscale != null) {
			this.codiceFiscale = codiceFiscale.toUpperCase();
		} else {
			this.codiceFiscale = "";
		}
	}
	
	public void setUserBean(UserViewBean userBean) {
		this.userBean = userBean;
	}
	public UserViewBean getUserBean() {
		return userBean;
	}
	public boolean equals(ClientViewBean other) {
		return getCodiceFiscale().equalsIgnoreCase(other.getCodiceFiscale()) &&
				getUserBean().getUsername().equalsIgnoreCase(other.getUserBean().getUsername());
	}
	public String toString() {
		return "ClientController"+
			   "\n\tName: "+getName()+
			   "\n\tSurname: "+getSurname()+
			   "\n\tCodice Fiscale: "+getCodiceFiscale()+
			   "\n"+getUserBean();
	}
}
