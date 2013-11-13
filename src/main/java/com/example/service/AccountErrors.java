package com.example.service;

public class AccountErrors extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AccountErrors(String errorMessage){
		super(errorMessage);
	}
}
