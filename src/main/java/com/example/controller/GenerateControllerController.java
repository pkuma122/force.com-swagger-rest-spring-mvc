package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.force.api.DescribeSObject;
import com.example.service.ControllerGenerator;
import com.example.service.LoginService;

@Controller
@RequestMapping("/controller")
public class GenerateControllerController {
	

	@Autowired
	LoginService loginService;
	
	@RequestMapping(value="")
	public String salesforceObjectModelView(Map<String, Object> map, Model model){
		map.put("sObject", loginService.getForceApi().describeGlobal().getSObjects());
		return "controllergenerator";
	}
	
	@RequestMapping(value="/generate",  method=RequestMethod.POST)
	public void generateSalesforceController(Map<String, Object> map, Object command, HttpServletRequest request, HttpServletResponse response) throws IOException{
		final ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(
				request);
		final Map<String, String> formData = new FormHttpMessageConverter()
				.read(null, inputMessage).toSingleValueMap();
		
		List<String> operationsList = new ArrayList<String>();
	
		String sobjectName = formData.get("sobject");
		String packageName = formData.get("packageName");
		
		File file = File.createTempFile(sobjectName, ".java");
		FileInputStream fileIn = new FileInputStream(file);
		OutputStream output = new FileOutputStream(file);
		ControllerGenerator cg = new ControllerGenerator();
		cg.generateController(output, packageName, sobjectName);
		
	    output.flush();
	    output.close();
	    try {
	    	IOUtils.copy(fileIn, response.getOutputStream());
	    	response.flushBuffer();
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
	     finally{ 
	    	 file.deleteOnExit();
	     }
	}
}
