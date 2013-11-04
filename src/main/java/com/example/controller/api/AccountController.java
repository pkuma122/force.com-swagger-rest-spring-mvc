package com.example.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.Account;
import com.example.service.AccountService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/api/v1/account")
@Api(value = "Account operations", listingClass = "AccountController", basePath = "/api/v1/account", description = "All operations for accounts")
public class AccountController {
	
	 @Autowired
	 AccountService accountService;
	
	@ApiOperation(value = "Get all accounts", notes = "Get all account (max:200)", httpMethod = "GET", responseClass = "Account", multiValueResponse = true)
	@ApiError(code = 500, reason = "Process error")
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Account> showAllAccounts() {
	    return accountService.listAccounts();
	}
	
	@ApiOperation(value = "Get account by Id", notes = "Get account by specifying Id", httpMethod = "GET", responseClass = "Account", multiValueResponse = true)
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid Id supplied"), @ApiError(code = 404, reason = "Account not found") })
	@RequestMapping(value = "/find/{accountId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Account findAccountById(@ApiParam(internalDescription = "java.lang.string", name = "accountId", required = true, value = "string") @PathVariable String accountId) {
		return accountService.findAccountById(accountId);
    }
	
	@ApiOperation(value = "Delete a account", notes = "Delete a specific account with the given Id", httpMethod = "DELETE")
    @ApiError(code = 500, reason = "Process error")
    @RequestMapping(value = "/delete/{accountId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody String deleteAccount(@ApiParam(internalDescription = "java.lang.string", name = "accountId", required = true, value = "string") @PathVariable String accountId) {
		return accountService.deleteAccount(accountId);
    }
	
	@ApiOperation(value = "Create a account using Parameters", notes = "Creates a new account in salesforce using Param", httpMethod = "POST")
    @ApiError(code = 500, reason = "Process error")
    @RequestMapping(value = "/new", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createAccountFromParamName(@ApiParam(internalDescription = "java.lang.string", name = "name", required = true, value = "string") @RequestParam(required = true) String name) {	
		return accountService.createAccount(name);
    }
	
	@ApiOperation(value = "Create a account using JSON", notes = "Creates a new account in salesforce", httpMethod = "POST")
    @ApiError(code = 500, reason = "Process error")
    @RequestMapping(value = "/new/json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody String createAccountFromJSON(@ApiParam(internalDescription = "com.example.model.Account", value="Account", name = "account", required = true) @RequestParam(required=true) Account account){	
		return accountService.createAccount(account);
    }
	
	@ApiOperation(value = "Update Account", notes = "Update a existing Account", httpMethod = "POST") 
	@RequestMapping(value = "/update/{accountId}", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String updateAccount(@ApiParam(internalDescription = "java.lang.string", name = "accountId", required = true, value = "string") @PathVariable String accountId, @ApiParam(internalDescription = "com.example.model.Account", value="Account", name = "account", required = true) @RequestParam(required=true) Account account) {
		
		System.out.println("Update Account: " + accountId + " " + account.getName());
		return accountService.updateAccount(accountId, account);
	}
}
