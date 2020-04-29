package com.objectway.stage.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionServiceBean {
	private Long id;
	private AccountServiceBean account;
	private boolean deposit;
	private BigDecimal amount;
	private LocalDate dateIns;
	
	public TransactionServiceBean() {
		super();
		this.id = Long.MIN_VALUE;
		this.dateIns = LocalDate.now();
		this.account = new AccountServiceBean();
	}

	public TransactionServiceBean(boolean deposit, BigDecimal amount) {
		this();
		this.deposit = deposit;
		this.amount = amount.abs();
	}

	public TransactionServiceBean(boolean deposit, BigDecimal importo, AccountServiceBean account) {
		this(deposit, importo);
		this.account = account;
	}

	public TransactionServiceBean(boolean deposit, BigDecimal importo, LocalDate dateIns, AccountServiceBean account) {
		this(deposit, importo, account);
		this.dateIns = dateIns;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public AccountServiceBean getAccount() {
		return account;
	}

	public void setAccount(AccountServiceBean account) {
		this.account = account;
	}
	
	public String toString() {
		return "Movimento "+getId()
				+"\nVersamento: "+isDeposit()
				+"\nImporto: "+getAmount()
				+"\ndata_ins: "+getDateIns()
				+"\nConto: "+getAccount();
	}
}
