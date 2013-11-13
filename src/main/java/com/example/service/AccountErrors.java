package com.example.service;

import com.force.api.ApiException;

public class AccountErrors extends ApiException {
	
	private static final long serialVersionUID = 1L;

	public AccountErrors(String errorMessage){
		super(500, errorMessage);
	}
}
