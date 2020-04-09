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

import com.objectway.stage.entity.UserEntity;

@Component
public class UserRepository {
	private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
	
	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Transactional
	public List<UserEntity> findAll() throws Exception{
		logger.info("Initialized method findAll()");
		List<UserEntity> users = new ArrayList<UserEntity>();
		String query = "SELECT u FROM user u";
		TypedQuery<UserEntity> queryResult = em.createQuery(query, UserEntity.class);
		users = queryResult.getResultList();
		logger.info("Executed query. Result: "+users);
		return users;
	}

	@Transactional
	public UserEntity findByUsername(String username) throws Exception{
		logger.info("Initialized method findByUsername()");
		UserEntity user = new UserEntity();
		String query = "FROM user u "
				+ "WHERE u.username = :username";
		TypedQuery<UserEntity> queryResult = em.createQuery(query, UserEntity.class);
		queryResult.setParameter("username", username);
		user = queryResult.getSingleResult();
		logger.info("Executed query. Result: "+user);
		return user;
	}

	@Transactional
	public void register(UserEntity user) throws Exception {
		logger.info("Initialized method register()");
		user.setPassword(HashUtils.hash(user.getPassword()));
		this.em.persist(user);
		this.em.flush();
		logger.info("Executed query");
	}

	@Transactional
	public void remove(UserEntity user) throws Exception {
		logger.info("Initialized method remove()");
		this.em.remove(user);
		logger.info("Executed query");	}
}
