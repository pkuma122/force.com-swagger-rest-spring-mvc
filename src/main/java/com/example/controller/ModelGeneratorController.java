package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.force.api.DescribeSObject;
import com.example.service.LoginService;
import com.example.service.ModelGenerator;

@Controller
@RequestMapping("/model")
public class ModelGeneratorController {
	
	@Autowired
	LoginService loginService;
	
	@RequestMapping(value="")
	public String salesforceObjectModelView(Map<String, Object> map){
		map.put("sObject", loginService.getForceApi().describeGlobal().getSObjects());
		return "modelgenerator";
	}
	
	@RequestMapping(value="/generate",  method=RequestMethod.POST)
	public void generateSalesforceModel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException{
		final ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(
				request);
		final Map<String, String> formData = new FormHttpMessageConverter()
				.read(null, inputMessage).toSingleValueMap();
		
		boolean boolCustomfields = false;
		String sobject = formData.get("sobject");
		String version = formData.get("version");
		String packageName = formData.get("packageName");
		String customFields = formData.get("customfields");
		if (customFields == "on")
			boolCustomfields = true;
		
		DescribeSObject ds = loginService.getForceApi().describeSObject(sobject);
    	
		File file = File.createTempFile(sobject, ".java");
		FileInputStream fileIn = new FileInputStream(file);
		OutputStream output = new FileOutputStream(file);
		ModelGenerator cgs = new ModelGenerator();
	    cgs.generateCodeStandardAndCustomFields(output, ds, version, packageName, boolCustomfields);
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
