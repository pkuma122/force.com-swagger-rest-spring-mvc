package com.example.model;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Account implements java.io.Serializable{
		
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value="Id")
	String id;

	@JsonProperty(value="Name")
	String name;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
