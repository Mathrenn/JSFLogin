package com.objectway.stage.viewbeans;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountViewBean {
		private Long id;
		private ClientViewBean client;
		private BigDecimal balance;
		private LocalDate dateIns;

		public AccountViewBean() {
			super();
			this.id = Long.MIN_VALUE;
		}
		
		public AccountViewBean(ClientViewBean client, BigDecimal balance, LocalDate date) {
			this();
			this.client = client;
			this.balance = balance;
			this.dateIns = date;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public ClientViewBean getClient() {
			return client;
		}

		public void setClient(ClientViewBean client) {
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
		public String toString() {
			return "Account "+getId()+
				   "\n\tBalance: "+getBalance()+
				   "\n\tCreation date: "+getDateIns()+
				   "\n"+getClient();
		}
}
