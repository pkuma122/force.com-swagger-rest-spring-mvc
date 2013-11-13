package com.example.service;

import java.util.List;

import com.example.model.Account;

public interface AccountService {
	List<Account> listAccounts();
	String findAccountById(String accountToFind);
	String deleteAccount(String accountToDelete);
	String createAccount(String name);
	String createAccount(Account newAccount);
	String updateAccount(String accountId, Account accountToUpdate);
}
