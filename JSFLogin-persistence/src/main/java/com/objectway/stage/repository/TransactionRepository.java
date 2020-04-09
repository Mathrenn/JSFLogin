package com.objectway.stage.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.objectway.stage.entity.AccountEntity;
import com.objectway.stage.entity.TransactionEntity;

@Component
public class TransactionRepository {
	private static final Logger logger = LoggerFactory.getLogger(TransactionRepository.class);

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Autowired
	private AccountRepository accountRepository;
	
	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Transactional
	public void register(TransactionEntity transaction) throws Exception {
		logger.info("Initialized method register()");
		this.em.persist(transaction);
		this.em.flush();
		logger.info("Executed query");
	}

	@Transactional
	public void remove(TransactionEntity transaction) throws Exception {
		logger.info("Initialized method remove()");
		this.em.remove(transaction);
		logger.info("Executed query");	
	}

	public List<TransactionEntity> findByAccountAndDate(AccountEntity account, LocalDate date) {
		logger.info("Initialized method findByAccountAndDate()");
		List<TransactionEntity> transactions = new ArrayList<>();
		String query = "SELECT t "
				+ "FROM transaction AS t "
				+ "WHERE t.account = :account AND "
				+ "t.dateIns <= :date";
		TypedQuery<TransactionEntity> queryResult = em.createQuery(query, TransactionEntity.class);
		queryResult.setParameter("account", account);
		queryResult.setParameter("date", date);
		transactions = queryResult.getResultList();
		logger.info("Executed query");
		logger.debug("Result: "+ transactions.stream().map(c -> c.toString()).collect(Collectors.toList()));
		return transactions;
	}

	public List<TransactionEntity> findByAccount(AccountEntity account) {
		logger.info("Initialized method findByAccount()");
		List<TransactionEntity> transactions = new ArrayList<TransactionEntity>();
		String query = "SELECT t "
				+ "FROM transaction AS t "
				+ "WHERE t.account = :account";
		TypedQuery<TransactionEntity> queryResult = em.createQuery(query, TransactionEntity.class);
		queryResult.setParameter("account", account);
		transactions = queryResult.getResultList();
		logger.info("Executed query");
		logger.debug("Result: "+ transactions.stream().map(c -> c.toString()).collect(Collectors.toList()));
		return transactions;
	}
}
