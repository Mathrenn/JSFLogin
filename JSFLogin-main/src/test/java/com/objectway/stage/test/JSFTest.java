package com.objectway.stage.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.objectway.stage.converter.AccountViewConverter;
import com.objectway.stage.converter.ClientViewConverter;
import com.objectway.stage.converter.UserViewConverter;
import com.objectway.stage.model.AccountServiceBean;
import com.objectway.stage.model.ClientServiceBean;
import com.objectway.stage.model.TransactionServiceBean;
import com.objectway.stage.model.UserServiceBean;
import com.objectway.stage.service.AccountService;
import com.objectway.stage.service.ClientService;
import com.objectway.stage.service.UserService;
import com.objectway.stage.viewbeans.AccountViewBean;
import com.objectway.stage.viewbeans.ClientViewBean;
import com.objectway.stage.viewbeans.UserViewBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class JSFTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	private UserViewBean uvb = new UserViewBean("user1", "User123_");
	private ClientViewBean cvb = new ClientViewBean("name", "surname", "codiceFiscale", uvb);

	@Autowired
	private UserService userService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private AccountService accountService;

	@Test
	public void userServiceTest() {
		UserViewConverter userConverter = new UserViewConverter();
		UserServiceBean user = userConverter.viewToService(uvb);
		assertTrue(userService!=null);
		assertFalse(userService.login(user));
		UserServiceBean registeredUser = userService.register(user);  
		assertTrue(registeredUser.equals(user));
		assertTrue(userConverter.serviceToView(registeredUser).equals(uvb));
		assertFalse(userService.register(user).equals(user));
		
		userService.getUserManager().getUserRepository().getEm().clear();
		
		assertTrue(userService.login(user));
	}
	
	@Test
	public void clientTest() {
		
		UserViewConverter userConverter = new UserViewConverter();
		UserServiceBean user = userConverter.viewToService(uvb);
		userService.register(user);
		assertTrue(userService.login(user));

		assertTrue(clientService!=null);
		assertTrue(clientService.findByUserName(uvb.getUsername()).isEmpty());

		ClientViewConverter clientConverter = new ClientViewConverter();
		ClientServiceBean client = clientService.register(clientConverter.viewToService(cvb));
		ClientViewBean cv = clientConverter.serviceToView(
				clientService.register(clientConverter.viewToService(cvb)));
		assertFalse(cv.equals(clientConverter.serviceToView(client)));

		assertTrue(clientConverter.serviceToView(client).equals(cvb));
		assertFalse(clientConverter.serviceToView(
				clientService.register(clientConverter.viewToService(cvb)))
				.equals(cvb));

		clientService.getClientManager().getClientRepository().getEm().clear();

		user.setUsername("user2");
		assertTrue(userService.register(user).equals(user));
		assertTrue(userService.login(user));

		client.setUser(user);
		client.setCodiceFiscale("codiceFiscaleDue");
		assertTrue(clientService.register(client).equals(client));
		client.setCodiceFiscale("codiceFiscaleTre");
		assertTrue(clientService.register(client).equals(client));

		List<ClientServiceBean> clients = clientService.findByUserName(user.getUsername());
		assertTrue(clients.size()==2);
		assertTrue(clients.get(0).getName().equals(client.getName()));
		assertTrue(clients.get(0).getSurname().equals(client.getSurname()));
		assertTrue(clients.get(0).getCodiceFiscale().equals("codiceFiscaleDue"));
		assertTrue(clients.get(1).getCodiceFiscale().equals(client.getCodiceFiscale()));
	}
	
	@Test
	public void accountTest() {
		UserViewConverter userConverter = new UserViewConverter();
		UserServiceBean user = userConverter.viewToService(uvb);
		userService.register(user);
		assertTrue(userService.login(user));

		ClientViewConverter clientConverter = new ClientViewConverter();
		ClientServiceBean client = clientService.register(clientConverter.viewToService(cvb));
		assertTrue(clientConverter.serviceToView(client).equals(cvb));

		AccountViewConverter contoCorrenteConverter = new AccountViewConverter();
		AccountViewBean ccvb = new AccountViewBean(cvb, new BigDecimal(0.0), LocalDate.now());
		AccountServiceBean ccsb = contoCorrenteConverter.viewToService(ccvb);

		AccountServiceBean conto = accountService.addDefaultAccount(client, LocalDate.now());
		ClientServiceBean cliente = conto.getClient();
		assertTrue(cliente.getCodiceFiscale().equals(ccsb.getClient().getCodiceFiscale()));
		assertTrue(cliente.getUser().getUsername().equals(ccsb.getClient().getUser().getUsername()));
		assertTrue(accountService.findByClient(conto.getClient()).size()==1);
		
		TransactionServiceBean mov = new TransactionServiceBean(true, new BigDecimal(100.0), conto);
		ccsb = accountService.addTransaction(mov);
		assertTrue(ccsb.equals(conto));
		ccsb.setTransactionList(accountService.findAllTransactionsByAccount(ccsb).stream().collect(Collectors.toSet()));
		assertFalse(ccsb.getTransactionList().isEmpty());
		assertTrue(accountService.getBalanceToDate(ccsb, LocalDate.now()).compareTo(BigDecimal.valueOf(100.0))==0);
		assertTrue(accountService.getMeanBalanceToDate(ccsb, LocalDate.now()).compareTo(BigDecimal.valueOf(100.0))==0);
		assertTrue(accountService.getMeanBalanceToDate(ccsb, LocalDate.now(), LocalDate.now().plusDays(5)).compareTo(BigDecimal.valueOf(100.0))==0);
		
		assertTrue(accountService.getDepositCountToDate(ccsb, LocalDate.now())==1);
		assertTrue(accountService.getWithdrawalCountToDate(ccsb, LocalDate.now())==0);

		ccsb = accountService.addTransaction(new TransactionServiceBean(false, new BigDecimal(50.0), LocalDate.now().plusDays(1), conto));
		ccsb = accountService.addTransaction(new TransactionServiceBean(true, new BigDecimal(100.0), LocalDate.now().plusDays(1), conto));
		ccsb.setTransactionList(accountService.findAllTransactionsByAccount(ccsb).stream().collect(Collectors.toSet()));
		assertTrue(accountService.findAllTransactionsByAccount(ccsb).size()==3);
		assertTrue(accountService.getBalanceToDate(ccsb, LocalDate.now()).compareTo(BigDecimal.valueOf(100.0))==0);
		assertTrue(accountService.getBalanceToDate(ccsb, LocalDate.now().plusDays(1)).compareTo(BigDecimal.valueOf(150.0))==0);

		assertTrue(accountService.getMeanBalanceToDate(ccsb, LocalDate.now()).compareTo(BigDecimal.valueOf(100.0))==0);
		assertTrue(accountService.getMeanBalanceToDate(ccsb, LocalDate.now().plusDays(1)).compareTo(BigDecimal.valueOf(125.0))==0);
		assertTrue(accountService.getMeanBalanceToDate(ccsb, LocalDate.now().plusDays(1), LocalDate.now()).compareTo(BigDecimal.valueOf(125.0))==0);
		assertTrue(accountService.getMeanBalanceToDate(ccsb, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5)).compareTo(BigDecimal.valueOf(150.0))==0);

		assertTrue(accountService.getDepositCountToDate(ccsb, LocalDate.now())==1);
		assertTrue(accountService.getDepositCountToDate(ccsb, LocalDate.now().plusDays(1))==2);
		assertTrue(accountService.getWithdrawalCountToDate(ccsb, LocalDate.now())==0);
		assertTrue(accountService.getWithdrawalCountToDate(ccsb, LocalDate.now().plusDays(1))==1);
	}
}
