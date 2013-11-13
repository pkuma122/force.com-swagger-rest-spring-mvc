package com.example.model;

public class RESTErrorInfo {
	
	 public final String url;
	 public final String ex;

	 public RESTErrorInfo(String url, Exception ex) {
	        this.url = url;
	        this.ex = ex.getLocalizedMessage();
	 }
}
