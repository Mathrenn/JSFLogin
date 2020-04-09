package com.objectway.stage.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objectway.stage.converter.ClientEntityConverter;
import com.objectway.stage.entity.ClientEntity;
import com.objectway.stage.entity.UserEntity;
import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.repository.ClientRepository;
import com.objectway.stage.repository.UserRepository;

@Component
public class ClientManager {
	private static final Logger logger = LoggerFactory.getLogger(ClientManager.class);
	
	private static final ClientEntityConverter converter = new ClientEntityConverter();

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public ClientRepository getClientRepository() {
		return clientRepository;
	}

	public void setClientRepository(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public ClientServiceBean addClient(ClientServiceBean client) {
		logger.info("Initialized method ClientManager.addClient()");
		ClientServiceBean cs = new ClientServiceBean();
		try {
			ClientEntity clientEntity = converter.serviceToEntity(client);
			clientEntity.setUser(userRepository.findByUsername(client.getUser().getUsername()));
			clientRepository.register(clientEntity);
			cs = converter.entityToService(clientEntity);
			logger.info("Transaction completed");
			logger.debug("Saved entity " + clientEntity);
		} catch(Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
			cs = new ClientServiceBean();
		}
		logger.debug("client is "+cs);
		return cs;
	}

	public boolean removeClient(ClientServiceBean user) {
		logger.info("Initialized method ClientManager.removeClient()");
		try {
			clientRepository.remove(converter.serviceToEntity(user));
			logger.info("Transaction completed");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
			return false;
		}
		return true;
	}
	
	public List<ClientServiceBean> findByUsername(String username) {
		logger.info("Initialized method ClientManager.findByUsername()");
		List<ClientServiceBean> clients = new ArrayList<>();
		try {
			UserEntity user = userRepository.findByUsername(username);
			clients = converter.entityListToServiceList(
					converter.entityIterToList(
							clientRepository.findByUser(user)));
			logger.info("Transaction completed");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
		}
		return clients;
	}
}
