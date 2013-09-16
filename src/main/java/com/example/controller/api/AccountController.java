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
	
	 @ApiOperation(value = "Find all accounts", notes = "Get all account currently available", httpMethod = "GET", responseClass = "Account", multiValueResponse = true)
	 @ApiError(code = 500, reason = "Process error")
	 @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	 public @ResponseBody List<Account> showAllAccounts(final HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
	    return accountService.listAccounts();
	}
	
	@ApiOperation(value = "Find account by Id", notes = "Get account by specifying Id", httpMethod = "GET", responseClass = "Account", multiValueResponse = true)
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid ID supplied"), @ApiError(code = 404, reason = "Account not found") })
	@RequestMapping(value = "/{accountId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Account findAccountById(@ApiParam(internalDescription = "java.lang.string", name = "accountId", required = true, value = "string") @PathVariable String accountId, final HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return accountService.findAccountById(accountId);
    }
	
	@ApiOperation(value = "Delete a account", notes = "Delete a specific account with the given ID", httpMethod = "DELETE")
    @ApiError(code = 500, reason = "Process error")
    @RequestMapping(value = "/{accountId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody String deleteAccount(@ApiParam(internalDescription = "java.lang.string", name = "accountId", required = true, value = "string") @PathVariable String accountId, final HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return accountService.deleteAccount(accountId);
    }
	
	@ApiOperation(value = "Create a account using Parameters", notes = "Creates a new account in salesforce using Param", httpMethod = "POST")
    @ApiError(code = 500, reason = "Process error")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createAccountFromParamName(@ApiParam(internalDescription = "java.lang.string", value="string", name = "Name", required = false) @RequestParam(required = true) String name, final HttpServletResponse response) {	
		response.addHeader("Access-Control-Allow-Origin", "*");
		return accountService.createAccount(name);
    }
	
	@ApiOperation(value = "Create a account using JSON", notes = "Creates a new account in salesforce", httpMethod = "POST")
    @ApiError(code = 500, reason = "Process error")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody String createAccountFromJSON(@RequestBody Account newAccount, final HttpServletResponse response){	
		response.addHeader("Access-Control-Allow-Origin", "*");
		return accountService.createAccount(newAccount);
    }
	
	@ApiOperation(value = "Update Account", notes = "Update a existing Account", httpMethod = "POST") 
	@RequestMapping(value = "/{accountId}", method = RequestMethod.POST, consumes = "application/json")
	 public @ResponseBody String updateAccount(@PathVariable String accountId, @RequestBody Account account, final HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return accountService.updateAccount(accountId, account);
	 }
}
