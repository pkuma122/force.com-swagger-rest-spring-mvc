package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Account;
import com.force.sdk.oauth.exception.ForceOAuthSessionExpirationException;

@Service
public class AccountServiceImpl implements AccountService{

	@Autowired 
	LoginService loginService;
	
	@Override
	public List<Account> listAccounts() {
		try {
			return loginService.getForceApi().query("SELECT Id, Name FROM Account", Account.class).getRecords();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Account findAccountById(String accountToFind) {
		try {
			return loginService.getForceApi().getSObject("account", accountToFind).as(Account.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String deleteAccount(String accountToDelete) {
		try{
			loginService.getForceApi().deleteSObject("account", accountToDelete);
			return "{status: success}";
		}catch(ForceOAuthSessionExpirationException e){
			return "{status : login failure}";
		}catch (Exception e){
			return "{status : failure}";
		}
	}

	@Override
	public String createAccount(String name) {
		try {
			Account newAccount = new Account();
			newAccount.setName(name);
	    	String id = loginService.getForceApi().createSObject("Account", newAccount);
	    	return "{id:" + id +"}";
		} catch (Exception e) {
			return "{status: failure}";
		}
	}

	@Override
	public String createAccount(Account newAccount) {
		try {
			String id = loginService.getForceApi().createSObject("Account", newAccount);
	    	return "{id:" + id +"}";
		} catch (Exception e) {
			return "{status: failure}";
		}
	}

	@Override
	public String updateAccount(String accountId, Account accountToUpdate) {
		try {
			 loginService.getForceApi().updateSObject("Account", accountId, accountToUpdate);
			 return "{status: success}";
		} catch (Exception e) {
			return "{status: failure}";
		}
	}
}
