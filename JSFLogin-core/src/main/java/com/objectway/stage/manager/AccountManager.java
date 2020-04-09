package com.objectway.stage.manager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.objectway.stage.converter.AccountEntityConverter;
import com.objectway.stage.converter.ServiceEntityConverter;
import com.objectway.stage.entity.AccountEntity;
import com.objectway.stage.entity.ClientEntity;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.repository.AccountRepository;
import com.objectway.stage.repository.ClientRepository;

@Component
public class AccountManager {
	private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);
	private static final ServiceEntityConverter<AccountServiceBean, AccountEntity> accountConverter = new AccountEntityConverter();

	@Autowired
	private ClientRepository clientRepository;
	
	public ClientRepository getClientRepository() {
		return clientRepository;
	}

	public void setClientRepository(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Autowired
	private AccountRepository accountRepository;

	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public AccountServiceBean findById(Long id) {
		logger.info("Initialized method AccountManager.findById()");
		AccountServiceBean account = new AccountServiceBean();
		try {
			account = accountConverter.entityToService(accountRepository.findById(id));
			logger.info("Transaction completed");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
		}
		return account;
	}
	
	public AccountServiceBean add(AccountServiceBean as) {
		logger.info("Initialized method AccountManager.add()");
		AccountServiceBean account = new AccountServiceBean();
		try {
			AccountEntity ae = accountConverter.serviceToEntity(as);
			ae.setClient(clientRepository.findByCodiceFiscale(as.getClient().getCodiceFiscale()));
			accountRepository.register(ae);
			account = accountConverter.entityToService(ae);
			logger.info("Transaction completed");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
		}
		return account;
	}
	
	public AccountServiceBean addDefault(ClientServiceBean client) {
		logger.info("Initialized method AccountManager.addDefault()");
		AccountServiceBean account = new AccountServiceBean();
		try {
			AccountEntity ae = new AccountEntity(clientRepository.findByCodiceFiscale(client.getCodiceFiscale()), 
					BigDecimal.valueOf(0.0), 
					LocalDate.now());
			logger.debug(ae.toString());
			accountRepository.register(ae);
			account = accountConverter.entityToService(ae);
			logger.info("Transaction completed");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
		}
		return account;
	}
	
	@Transactional
	public List<AccountServiceBean> findByClient(ClientServiceBean client) {
		logger.info("Initialized method AccountManager.findByClient()");
		List<AccountServiceBean> accounts = new ArrayList<>();
		try {
			ClientEntity ce = clientRepository.findByCodiceFiscale(client.getCodiceFiscale());
			accounts = accountConverter.entityListToServiceList(
						accountRepository.findByClient(ce));
			logger.info("Transaction completed");
		} catch (Exception e) {
			logger.error("Transaction failed " + e.getClass().getSimpleName());
		}
		return accounts;
	}
}
