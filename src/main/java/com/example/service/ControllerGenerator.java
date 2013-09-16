package com.example.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Simple Swagger Controller Generator
 *
 * @author Thys Michels
 */

@Service
public class ControllerGenerator {
	
	static final String NEWLINE = "\r\n";
	static final String TAB = "    ";
	
	private final void write(OutputStream out, String content) throws IOException {
    	out.write(content.getBytes("UTF-8"));
	}
	
	 public boolean generateController(OutputStream out, String packageName, String sObjectName, List<String> operations) throws IOException {
		   
		 	String SObjectNameToLowerCase =sObjectName.substring(0,1).toLowerCase() + sObjectName.substring(1, sObjectName.length());
		 	
		   write(out,"package " + packageName + ";" + NEWLINE + NEWLINE);
		   
		   write(out,"import java.util.List;" + NEWLINE);
		   write(out,"import javax.annotation.PostConstruct;" + NEWLINE);
		   write(out,"import javax.inject.Inject;" + NEWLINE);
		   write(out,"import javax.inject.Named;" + NEWLINE);
		   write(out,"import org.springframework.stereotype.Controller;" + NEWLINE);
		   write(out,"import org.springframework.web.bind.annotation.PathVariable;" + NEWLINE);
		   write(out,"import org.springframework.web.bind.annotation.RequestBody;" + NEWLINE);
		   write(out,"import org.springframework.web.bind.annotation.RequestMapping;" + NEWLINE);
		   write(out,"import org.springframework.web.bind.annotation.RequestMethod;" + NEWLINE);
		   write(out,"import org.springframework.web.bind.annotation.RequestParam;" + NEWLINE);
		   write(out,"import org.springframework.web.bind.annotation.ResponseBody;" + NEWLINE);
		   write(out,"import com.force.api.ForceApi;" + NEWLINE);
		   write(out,"import com.force.api.QueryResult;" + NEWLINE);
		   write(out,"import " + packageName + "."  + sObjectName + ";" + NEWLINE);
		   write(out,"import " + packageName + " .SalesforceLogin;" + NEWLINE);
		   write(out,"import com.wordnik.swagger.annotations.Api;" + NEWLINE);
		   write(out,"import com.wordnik.swagger.annotations.ApiError;" + NEWLINE);
		   write(out,"import com.wordnik.swagger.annotations.ApiErrors;" + NEWLINE);
		   write(out,"import com.wordnik.swagger.annotations.ApiOperation;" + NEWLINE);
		   write(out,"import com.wordnik.swagger.annotations.ApiParam;" + NEWLINE + NEWLINE + NEWLINE);
		   write(out, "@Controller" + NEWLINE);
		   write(out, "@RequestMapping(value = \"/api/v1/" + SObjectNameToLowerCase + "\")" + NEWLINE);
		   write(out, "@Api(value = \"" + sObjectName + " operations\", listingClass = \"" + sObjectName + " Controller\", basePath = \"/api/v1/" + SObjectNameToLowerCase + "\", description = \"All operations for " +  sObjectName + "\")" + NEWLINE);
		   write(out, "public class " + sObjectName + "Controller {" + NEWLINE + NEWLINE);
		   write(out, TAB + "@Inject" + NEWLINE);
		   write(out, TAB + "@Named(\"SalesforceLogin\")" + NEWLINE);
		   write(out, TAB + "private SalesforceLogin sfdcLogin;" + NEWLINE);
		   write(out, TAB + "private ForceApi api;" + NEWLINE + NEWLINE);
		   write(out, TAB + "@PostConstruct" + NEWLINE);
		   write(out, TAB + "public void init(){" + NEWLINE);
		   write(out, TAB + TAB + "api = sfdcLogin.loginToSalesforce();" + NEWLINE);
		   write(out, TAB + "}" + NEWLINE + NEWLINE);
		   
		   if (operations.contains("All")){
			   write(out, TAB + "@ApiOperation(value = \"Find all " + sObjectName + "s\", notes = \"Get all " + sObjectName + " currently available\", httpMethod = \"GET\", responseClass = \""+ sObjectName+ "\", multiValueResponse = true)" + NEWLINE);
			   write(out, TAB + "@ApiError(code = 500, reason = \"Process error\")" + NEWLINE);
			   write(out, TAB + "@RequestMapping(value = \"/\", method = RequestMethod.GET, produces = \"application/json\")" + NEWLINE);
			   write(out, TAB + "public @ResponseBody List<" + sObjectName +"> getAll" + sObjectName + "s() {" + NEWLINE);
			   //Build Select Query
			   write(out, TAB + TAB + "QueryResult<"+ sObjectName +"> res = api.query(\"SELECT Name FROM "+ sObjectName +"\", " + sObjectName + ".class);" + NEWLINE);
			   write(out, TAB + TAB + "return res.getRecords();" + NEWLINE);
			   write(out, TAB + "}" + NEWLINE + NEWLINE);
		   }
		   if (operations.contains("FindById")){
			   write(out, TAB + "@ApiOperation(value = \"Find " + sObjectName + " by Id\", notes = \"Find " + sObjectName +  " by specifying Id\", httpMethod = \"GET\", responseClass = \""+sObjectName+"\", multiValueResponse = true)" + NEWLINE);
			   write(out, TAB + "@ApiErrors(value = { @ApiError(code = 400, reason = \"Invalid id supplied\"), @ApiError(code = 404, reason = \"" + sObjectName + " not found\") })" + NEWLINE);
			   write(out, TAB + "@RequestMapping(value = \"/{"+ SObjectNameToLowerCase +"Id}\", method = RequestMethod.GET, produces = \"application/json\")" + NEWLINE);
			   write(out, TAB + "public @ResponseBody " +  sObjectName + "[] get"+sObjectName+"ById(@ApiParam(internalDescription = \"java.lang.string\", name = \""+ SObjectNameToLowerCase+"Id\", required = true, value = \"string\") @PathVariable String "+ SObjectNameToLowerCase + "Id) {" + NEWLINE);
			   write(out, TAB + TAB + sObjectName + " " + SObjectNameToLowerCase +" = api.getSObject(\""+SObjectNameToLowerCase+"\", " + SObjectNameToLowerCase + "Id).as("+sObjectName+".class);" + NEWLINE);
			   write(out, TAB + TAB + "return new "+sObjectName+"[]{" + SObjectNameToLowerCase + "};" + NEWLINE);
			   write(out, TAB + "}" + NEWLINE + NEWLINE);
		   }
		   if (operations.contains("Delete")){
			   write(out, TAB + "@ApiOperation(value = \"Delete a "+ sObjectName + "\", notes = \"Delete a specific " + sObjectName + " with the given id\", httpMethod = \"DELETE\")" + NEWLINE);
			   write(out, TAB + "@ApiError(code = 500, reason = \"Process error\")" + NEWLINE);
			   write(out, TAB + "@RequestMapping(value = \"/{"+SObjectNameToLowerCase+"Id}\", method = RequestMethod.DELETE, produces = \"application/json\")" + NEWLINE);
			   write(out, TAB + "public @ResponseBody String delete" + sObjectName +"(@ApiParam(internalDescription = \"java.lang.string\", name = \""+ SObjectNameToLowerCase +"Id\", required = true, value = \"string\") @PathVariable String " + SObjectNameToLowerCase + "Id) {" + NEWLINE);
			   write(out, TAB + TAB +"api.deleteSObject(\""+ SObjectNameToLowerCase +"\", "+SObjectNameToLowerCase+"Id);" + NEWLINE);
			   write(out, TAB + TAB + "return \"{status: success}\";" + NEWLINE);
			   write(out, TAB + "}" + NEWLINE + NEWLINE);
		   }
		   if (operations.contains("Add")){
			   write(out, TAB + "@ApiOperation(value = \"Add a " + sObjectName +  "\", notes = \"Add a new "+ sObjectName + "\", httpMethod = \"POST\")" + NEWLINE);
			   write(out, TAB + "@ApiError(code = 500, reason = \"Process error\")" + NEWLINE);
			   write(out, TAB + "@RequestMapping(method = RequestMethod.POST, consumes = \"application/json\", produces = \"application/json\")" + NEWLINE);
			   write(out, TAB + "public @ResponseBody String add" + sObjectName +"(@RequestBody " + sObjectName+" new"+ sObjectName +"){	" + NEWLINE);
			   write(out, TAB + TAB + "return api.createSObject(\""+sObjectName+"\", new"+sObjectName+");" + NEWLINE);
			   write(out, TAB + "}" + NEWLINE + NEWLINE);
		   }
		   if (operations.contains("Update")){
			   write(out, TAB + "@ApiOperation(value = \"Update "+sObjectName+"\", notes = \"Update a existing "+ sObjectName+"\", httpMethod = \"POST\") " + NEWLINE);
			   write(out, TAB + "@RequestMapping(value = \"/{"+SObjectNameToLowerCase+"Id}\", method = RequestMethod.POST, consumes = \"application/json\")" + NEWLINE);
			   write(out, TAB + "public @ResponseBody String update"+ sObjectName +"(@PathVariable String "+ SObjectNameToLowerCase +"Id, @RequestBody "+sObjectName+ " "+ SObjectNameToLowerCase+") {" + NEWLINE);
			   write(out, TAB + TAB + "api.updateSObject(\""+sObjectName+"\", "+SObjectNameToLowerCase+"Id, "+SObjectNameToLowerCase+");" + NEWLINE);
			   write(out, TAB + TAB + "return \"{status: success}\";" + NEWLINE);
			   write(out, TAB + "}" + NEWLINE + NEWLINE);
		   }
		
		   write(out, "}" + NEWLINE);
		  
		 return true;
	 }
	 public static void main(String[] args) throws IOException {
		 OutputStream output = new FileOutputStream(new File("/Users/thysmichels/Desktop/AccountController.java"));
		 ControllerGenerator cg = new ControllerGenerator();
		 List<String> operations = java.util.Arrays.asList("All", "FindById","Add", "Delete", "Update");
		 cg.generateController(output, "com.thysmichels.controller", "Account", operations);
	}
	 
}
