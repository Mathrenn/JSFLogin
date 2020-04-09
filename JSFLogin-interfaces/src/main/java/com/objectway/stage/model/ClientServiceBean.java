package com.objectway.stage.model;

public class ClientServiceBean {
	private Long id;
	private String name;
	private String surname;
	private String codiceFiscale;
	private UserServiceBean user;

	public ClientServiceBean() {
		super();
		this.id = Long.MIN_VALUE;
		this.name = "";
		this.surname = "";
		this.codiceFiscale = "";
		this.user = new UserServiceBean();
	}

	public ClientServiceBean(Long id) {
		this();
		this.id = id;
	}
	
	public ClientServiceBean(String name, String surname, String codiceFiscale, UserServiceBean user) {
		this();
		this.name = name;
		this.surname = surname;
		this.codiceFiscale = codiceFiscale;
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public UserServiceBean getUser() {
		return user;
	}
	public void setUser(UserServiceBean user) {
		this.user = user;
	}
	
	public boolean equals(ClientServiceBean other) {
		return getCodiceFiscale().equals(other.getCodiceFiscale()) && 
				getUser().getUsername().equals(other.getUser().getUsername());
	}
	
	public String toString() {
		return "Id: "+getId()+
				   "\nName: "+getName()+
				   "\nSurname: "+getSurname()+
				   "\nSSN: "+getCodiceFiscale()+
				   "\nUser: "+getUser();
	}
}
