package com.objectway.stage.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "client")
@Table(name = "CLIENT")
public class ClientEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Column(name = "NAME")
	String name;

	@Column(name = "SURNAME")
	String surname;

	@Column(name = "CODICEFISCALE", unique = true)
	String codiceFiscale;

	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
	Set<AccountEntity> accounts = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "clients", nullable = false)
	UserEntity user;

	public ClientEntity() {
		super();
	}
	
	public ClientEntity(String name, String surname, String codiceFiscale, UserEntity user) {
		super();
		this.name = name;
		this.surname = surname;
		this.codiceFiscale = codiceFiscale;
		this.user = user;
	}

	public Long getId() {
		return id;
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
	
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String toString() {
		return "Id: "+getId()+
			   "\nName: "+getName()+
			   "\nSurname: "+getSurname()+
			   "\nSSN: "+getCodiceFiscale()+
			   "\nUser: "+getUser();
	}
	
}
