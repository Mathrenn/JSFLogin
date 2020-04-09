package com.objectway.stage.viewbeans;

public class UserViewBean {
	private String username;
	private String password;
	private boolean loggedIn;
	
	public UserViewBean() {
		super();
		this.username = "";
		this.password = "";
		this.loggedIn = false;
	}
	public UserViewBean(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.loggedIn = false;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username != null) {
			this.username = username.toLowerCase();
		} else {
			this.username = "";
		}
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
	public boolean equals(UserViewBean other) {
		return this.username.equals(other.getUsername()) &&
				this.password.equals(other.getPassword());
	}
	public String toString() {
		return "User"+
			   "\n\tUsername: "+getUsername()+
			   "\n\tPassword: "+getPassword();
	}
}
