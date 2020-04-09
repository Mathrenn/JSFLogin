package com.objectway.stage.viewbeans;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TransactionViewBean implements Comparable<TransactionViewBean>{
	private AccountViewBean account;
	private boolean deposit;
	private BigDecimal amount;
	private LocalDate dateIns;
	
	public TransactionViewBean() {
		super();
	}

	public TransactionViewBean(boolean deposit, BigDecimal amount) {
		super();
		this.deposit = deposit;
		this.amount = amount.abs();
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

	public AccountViewBean getAccount() {
		return account;
	}

	public void setAccount(AccountViewBean account) {
		this.account = account;
	}
	
	// getter fittizio
	// usato in transactions.xhtml richiamando #{beanName.transactionType}
	public String getTransactionType() {
		String deposit = "";
		ResourceBundle messages = ResourceBundle.getBundle("messages/messages");
		if(isDeposit()) {
			deposit = messages.getString("transaction.type.deposit");
		} else {
			deposit = messages.getString("transaction.type.withdrawal");
		}
		return deposit;
	}
	
	// setter fittizio, parametro mai popolato
	public void setTransactionType(String depositString) {}

	public String toString() {
		return "Movimento"
				+"\n\tVersamento: "+isDeposit()
				+"\n\tImporto: "+getAmount()
				+"\n\tData Inserimento: "+getDateIns()
				+"\n"+getAccount();
	}

	@Override
	public int compareTo(TransactionViewBean o) {
		// defaults to descending order
		if(getDateIns().isBefore(o.getDateIns()))
			return 1;
		else if(getDateIns().isAfter(o.getDateIns()))
			return -1;
		else return 0;
	}
}
