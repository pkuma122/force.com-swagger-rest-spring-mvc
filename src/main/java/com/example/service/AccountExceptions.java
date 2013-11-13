package com.example.service;

public final class AccountExceptions extends Exception {

	private static final long serialVersionUID = 1L;

	public AccountExceptions(){
		super("Bad Request");
	}
}
