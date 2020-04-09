package com.objectway.stage.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.objectway.stage.entity.AccountEntity;
import com.objectway.stage.entity.ClientEntity;

@Component
public class AccountRepository {
	private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Autowired
	private ClientRepository clientRepository;
	
	public ClientRepository getClientRepository() {
		return clientRepository;
	}

	public void setClientRepository(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Transactional
	public AccountEntity findById(Long id) throws Exception{
		AccountEntity cce = new AccountEntity();
		String query = "SELECT a "
				+ "FROM account a "
				+ "WHERE a.id = :id";
		TypedQuery<AccountEntity> queryResult = em.createQuery(query, AccountEntity.class);
		queryResult.setParameter("id", id);
		cce = queryResult.getSingleResult();
		logger.info("Executed query");
		return cce;
	}

	public List<AccountEntity> findByClient(ClientEntity client) throws Exception{
		List<AccountEntity> accounts = new ArrayList<>();
		String query = "SELECT a "
				+ "FROM account a "
				+ "JOIN FETCH a.client "
				+ "WHERE a.client = :client";
		TypedQuery<AccountEntity> queryResult = em.createQuery(query, AccountEntity.class);
		queryResult.setParameter("client", client);
		accounts = queryResult.getResultList();
		logger.info("Executed query");
		logger.debug("Found "+accounts.size()+" accounts");
		return accounts;
	}
	
	@Transactional
	public void register(AccountEntity account) throws Exception {
		logger.info("Initialized method register()");
		this.em.persist(account);
		this.em.flush();
		logger.info("Executed query");
	}

	@Transactional
	public void remove(AccountEntity conto) throws Exception {
		logger.info("Initialized method remove()");
		this.em.remove(conto);
		logger.info("Executed query");	
	}
}
