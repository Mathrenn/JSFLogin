package com.objectway.stage.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.objectway.stage.entity.ClientEntity;
import com.objectway.stage.entity.UserEntity;

@Component
public class ClientRepository {
	private static final Logger logger = LoggerFactory.getLogger(ClientRepository.class);
	
	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Transactional
	public ClientEntity findByCodiceFiscale(String codiceFiscale) throws Exception{
		logger.info("Initialized method findByCodiceFiscale()");
		ClientEntity client = new ClientEntity();
		String query = "SELECT c "
				+ "FROM client c "
				+ "WHERE c.codiceFiscale = :codiceFiscale";
		TypedQuery<ClientEntity> queryResult = em.createQuery(query, ClientEntity.class);
		queryResult.setParameter("codiceFiscale", codiceFiscale);
		client = queryResult.getSingleResult();
		logger.info("Executed query.");
		logger.debug("Client "+client.getName()+" found");
		return client;
	}
	
	@Transactional
	public List<ClientEntity> findByUser(UserEntity user) throws Exception{
		logger.info("Initialized method findByUsername()");
		List<ClientEntity> clients = new ArrayList<ClientEntity>();
		String query = "SELECT c "
				+ "FROM client c "
				+ "WHERE c.user = :user";
		TypedQuery<ClientEntity> queryResult = em.createQuery(query, ClientEntity.class);
		queryResult.setParameter("user", user);
		clients = queryResult.getResultList();
		logger.info("Executed query.");
		logger.debug("Found "+clients.size()+" clients");
		return clients;
	}

	@Transactional
	public void register(ClientEntity client) throws Exception {
		logger.info("Initialized method register()");
		this.em.persist(client);
		this.em.flush();
		logger.info("Executed query");
	}

	@Transactional
	public void remove(ClientEntity client) throws Exception {
		logger.info("Initialized method remove()");
		this.em.remove(client);
		logger.info("Executed query");	}
}
