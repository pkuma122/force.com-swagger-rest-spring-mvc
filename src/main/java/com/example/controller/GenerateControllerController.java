package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	public void generateSalesforceController(Map<String, Object> map, Object command, @RequestParam(value="sobject") String sobjectName, @RequestParam(value="packageName") String packageName, HttpServletResponse response) throws IOException{
		
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
