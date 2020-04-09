package com.objectway.stage.manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objectway.stage.converter.TransactionEntityConverter;
import com.objectway.stage.converter.ServiceEntityConverter;
import com.objectway.stage.entity.AccountEntity;
import com.objectway.stage.entity.TransactionEntity;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.TransactionServiceBean;
import com.objectway.stage.repository.AccountRepository;
import com.objectway.stage.repository.TransactionRepository;

@Component
public class TransactionsManager {
	private static final Logger logger = LoggerFactory.getLogger(TransactionsManager.class);
	private static final ServiceEntityConverter<TransactionServiceBean, TransactionEntity> transactionConverter = new TransactionEntityConverter();

	@Autowired
	private TransactionRepository transactionRepository;
	
	public TransactionRepository getTransactionRepository() {
		return transactionRepository;
	}

	public void setTransactionRepository(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@Autowired
	private AccountRepository accountRepository;
	
	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public TransactionServiceBean add(TransactionServiceBean ts) {
		logger.info("Initialized method TransactionsManager.add()");
		TransactionServiceBean transaction = new TransactionServiceBean();
		try {
			TransactionEntity te = transactionConverter.serviceToEntity(ts);
			te.setAccount(accountRepository.findById(ts.getAccount().getId()));
			transactionRepository.register(te);
			transaction = transactionConverter.entityToService(te);
			logger.info("Transaction completed");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
			transaction = new TransactionServiceBean();
		}
		return transaction;
	}
	
	@Transactional
	public List<TransactionServiceBean> findByAccountAndDate(AccountServiceBean account, LocalDate date) {
		logger.info("Initialized method TransactionsManager.findByAccountAndDate()");
		List<TransactionServiceBean> transactions = new ArrayList<>();
		try {
			AccountEntity ae = accountRepository.findById(account.getId());
			transactions = transactionConverter.entityListToServiceList(
					transactionRepository.findByAccountAndDate(ae, date));
			logger.info("Transaction completed");
			logger.debug("Found "+transactions.size()+" transactions");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
			e.printStackTrace();
		}
		return transactions;
	}

	@Transactional
	public List<TransactionServiceBean> findByAccount(AccountServiceBean account) {
		logger.info("Initialized method TransactionsManager.findByAccount()");
		List<TransactionServiceBean> transactions = new ArrayList<>();
		try {
			AccountEntity ae = accountRepository.findById(account.getId());
			transactions = transactionConverter.entityListToServiceList(
					transactionRepository.findByAccount(ae));
			logger.info("Transaction completed");
			logger.debug("Found "+transactions.size()+" transactions");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
		}
		return transactions;
	}
}
