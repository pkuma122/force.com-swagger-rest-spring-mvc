package com.example.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.model.Account;
import com.force.api.ApiException;
import com.force.api.ForceApi;

public class Main {

	public static void main(String[] args) {
		try{
			AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
	        applicationContext.getEnvironment().setActiveProfiles("default");
	        applicationContext.scan(LoginConfiguration.class.getPackage().getName());
	        applicationContext.refresh();
			     
			//Connecting to Force.com REST API
	        ForceApi api =applicationContext.getBean(ForceApi.class);
			
			//Setting account name using Account POJO
			Account a = new Account();
			a.setName("Test account");
			
			//CREATEING new Account and returning the Account ID
			String accountId = api.createSObject("account", a);
			System.out.println("*** Created New Account ID *** " + accountId);
			
			//READING new Account
			Account account = api.getSObject("Account",accountId).as(Account.class);
			System.out.println("*** Reading Account Name *** " + account.getName());
			
			//Update the Account with a new name
			a.setName("Updated Test Account");
			api.updateSObject("account", accountId, a);
			System.out.println("*** Updating Account Name *** " + api.getSObject("Account",accountId).as(Account.class).getName());
			
			//DELETING the Account
			api.deleteSObject("account", accountId);
			System.out.println("*** Delete Account *** " + api.getSObject("Account",accountId).as(Account.class).getName());
		
		}catch(ApiException e){
			System.out.println("*** ERROR *** " + e.getMessage());
		}
	}
}
