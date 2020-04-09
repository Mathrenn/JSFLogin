package com.objectway.stage.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "user")
@Table(name = "USER")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Column(name = "USERNAME", unique = true)
	String username;

	@Column(name = "PASSWORD")
	String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	Set<ClientEntity> clients = new HashSet<ClientEntity>();

	public UserEntity() {
		super();
		this.username = "";
	}

	public UserEntity(String username, String password) {
		super();
		this.username = username.toLowerCase();
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username.toLowerCase();
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

	public Set<ClientEntity> getClients() {
		return clients;
	}

	public void setClients(Set<ClientEntity> clients) {
		this.clients = clients;
	}
	
	public boolean equals(UserEntity other) {
		return this.getUsername().equals(other.getUsername()) && 
				this.getPassword().equals(other.getPassword());
	}
	
	public String toString() {
		return "User "+this.username;
	}
}
