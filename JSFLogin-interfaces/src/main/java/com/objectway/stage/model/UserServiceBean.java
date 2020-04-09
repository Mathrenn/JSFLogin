package com.objectway.stage.model;

public class UserServiceBean {
	private Long id;
	private String username;
	private String password;
	private boolean loggedIn;

	public UserServiceBean() {
		super();
		this.id=Long.MIN_VALUE;
		this.username="";
		this.password="";
		this.loggedIn = false;
	}
	public UserServiceBean(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean equals(UserServiceBean other) {
		return this.username.equals(other.getUsername()) &&
				this.password.equals(other.getPassword());
	}
	
	public String toString() {
		return "Username: " + getUsername()
				+ "\nPassword: " + getPassword()
				+ "\nLoggedIn: " + isLoggedIn();
	}
}
