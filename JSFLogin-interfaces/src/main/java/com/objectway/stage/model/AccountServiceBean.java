package com.objectway.stage.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AccountServiceBean {
	private Long id;
	private ClientServiceBean client;
	private BigDecimal balance;
	private LocalDate dateIns;
	private Set<TransactionServiceBean> transactionList;

	public AccountServiceBean() {
		super();
		id = Long.MIN_VALUE;
		client = new ClientServiceBean();
		transactionList = new HashSet<>();
	}

	public AccountServiceBean(Long id) {
		this();
		this.id = id;
	}

	public AccountServiceBean(ClientServiceBean clienteId, BigDecimal saldo, LocalDate date) {
		this();
		this.client = clienteId;
		this.balance = saldo;
		this.dateIns = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ClientServiceBean getClient() {
		return client;
	}

	public void setClient(ClientServiceBean client) {
		this.client = client;
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

	public Set<TransactionServiceBean> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(Set<TransactionServiceBean> transactionList) {
		this.transactionList = transactionList;
	}

	public void addTransaction(TransactionServiceBean transaction) {
		transactionList.add(transaction);
	}

	public boolean equals(AccountServiceBean other) {
		return this.id == other.getId() && 
				this.client.equals(other.getClient());
	}

	public String toString() {
		return "Conto "+ getId() 
		+ "\nData Apertura: "+getDateIns()
		+ "\nSaldo: "+getBalance()
		+ "\nCliente:\n"+getClient()
		+ "\nLista Movimenti: "+getTransactionList();
	}
}
