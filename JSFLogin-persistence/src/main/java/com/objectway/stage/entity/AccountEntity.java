package com.objectway.stage.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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

@Entity(name = "account")
@Table(name = "ACCOUNT")
public class AccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "BALANCE")
	private BigDecimal balance;

	@Column(name = "DATE_INS")
	private LocalDate dateIns;
	
	@ManyToOne
	@JoinColumn(name = "accounts", nullable = false)
	private ClientEntity client;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	Set<TransactionEntity> transactionList;

	public AccountEntity() {
		super();
		this.transactionList = new HashSet<>();
	}

	public AccountEntity(ClientEntity client, BigDecimal balance) {
		this();
		this.client = client;
		this.balance = balance;
	}

	public AccountEntity(ClientEntity client, BigDecimal balance, LocalDate date) {
		this();
		this.client = client;
		this.balance = balance;
		this.dateIns = date;
	}
	
	public Long getId() {
		return id;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public LocalDate getDateIns() {
		return dateIns;
	}

	public void setDateIns(LocalDate dateIns) {
		this.dateIns = dateIns;
	}

	public ClientEntity getClient() {
		return client;
	}

	public void setClient(ClientEntity client) {
		this.client = client;
	}

	public Set<TransactionEntity> getTransactionList() {
		return transactionList;
	}
	
	public void setTransactionList(Set<TransactionEntity> transactionList) {
		this.transactionList = transactionList;
	}
	
	public String toString() {
		return "Conto "+ getId() 
			+ "\nData Apertura: "+getDateIns()
			+ "\nSaldo: "+getBalance()
			+ "\nCliente:\n"+getClient();
	}
}
