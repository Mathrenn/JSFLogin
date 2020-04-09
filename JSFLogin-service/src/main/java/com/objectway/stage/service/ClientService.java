package com.objectway.stage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objectway.stage.manager.ClientManager;
import com.objectway.stage.model.ClientServiceBean;

@Component
public class ClientService {
	@Autowired
	ClientManager clientManager;

	public ClientManager getClientManager() {
		return clientManager;
	}

	public void setClientManager(ClientManager clientManager) {
		this.clientManager = clientManager;
	}

	public ClientServiceBean register(ClientServiceBean client) {
		return clientManager.addClient(client);
	}

	public List<ClientServiceBean> findByUserName(String username) {
		return clientManager.findByUsername(username);
	}
}
