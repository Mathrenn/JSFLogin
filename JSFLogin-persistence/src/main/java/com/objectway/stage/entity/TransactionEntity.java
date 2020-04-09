package com.objectway.stage.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "transaction")
@Table(name = "TRANSACTION")
public class TransactionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DEPOSIT")
	private boolean deposit;

	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@Column(name = "DATE_INS")
	private LocalDate dateIns;

	@ManyToOne
	@JoinColumn(name = "transactionList", nullable = false)
	private AccountEntity account;

	public TransactionEntity() {
		super();
	}

	public TransactionEntity(boolean deposit, BigDecimal amount) {
		super();
		this.deposit = deposit;
		this.amount = amount.abs();
	}
	
	public Long getId() {
		return id;
	}

	public boolean isDeposit() {
		return deposit;
	}

	public void setDeposit(boolean deposit) {
		this.deposit = deposit;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount.abs();
	}

	public LocalDate getDateIns() {
		return dateIns;
	}

	public void setDateIns(LocalDate dateIns) {
		this.dateIns = dateIns;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}
	
	public String toString() {
		return "Transaction "+getId()
				+ "\n\tDeposit: "+isDeposit()
				+ "\n\tAmount: "+getAmount()
				+ "\n\tCreation Date: "+getDateIns()
				+ "\n"+getAccount();
	}
}
