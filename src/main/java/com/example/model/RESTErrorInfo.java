package com.example.model;

public class RESTErrorInfo {
	
	 public final StringBuffer url;
	 public final String ex;

	 public RESTErrorInfo(StringBuffer stringBuffer, Exception ex) {
	        this.url = stringBuffer;
	        this.ex = ex.getLocalizedMessage();
	 }
}
