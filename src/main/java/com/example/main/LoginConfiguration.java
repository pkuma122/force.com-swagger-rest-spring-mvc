package com.example.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.force.api.ApiConfig;
import com.force.api.ForceApi;

@PropertySource("classpath:/salesforcelogin.properties")
@Configuration
public class LoginConfiguration {
	
	@Autowired Environment environment;
	
	@Bean
	public ForceApi loginToSalesforce(){
		if (environment.getProperty("SALESFORCEUSERNAME")!=null && environment.getProperty("SALESFORCEPASSWORD")!=null)
			return new ForceApi(new ApiConfig().setUsername(environment.getProperty("SALESFORCEUSERNAME")).setPassword(environment.getProperty("SALESFORCEPASSWORD")));
		return null;
	}

}
