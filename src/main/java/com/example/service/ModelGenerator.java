package com.example.service;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.force.api.ApiConfig;
import com.force.api.DescribeSObject;
import com.force.api.ForceApi;
import com.force.api.DescribeSObject.Field;

/**
 * Simple Swagger Model Generator
 *
 * @author Thys Michels
 */

@Service
public class ModelGenerator {
	
	static final String NEWLINE = "\r\n";
	static final String TAB = "    ";

	private final void write(OutputStream out, String content) throws IOException {
	    	out.write(content.getBytes("UTF-8"));
	}
	    
	 public boolean generateCodeStandardAndCustomFields(OutputStream out, DescribeSObject describe, String apiVersion, String packageName, boolean customfields) throws IOException {
	        if(describe == null) {
	            return false;
	        }
 
	        String className = describe.getName();
	        if(describe.isCustom()) {
	        	className = className.substring(0,className.length()-3);
	        }

	        if(packageName == null || packageName.isEmpty()) {
	        	if(describe.isCustom()) {
	        		packageName = "org.example.sobject";
	        	} else {
	        		packageName = "com.salesforce.sobject";
	        	}
	        }
	        
	        Set<String> fieldNames = new HashSet<String>();
	        for(Field f : describe.getFields()) {
	        	if (customfields)
	        		fieldNames.add(f.getName());
	        	else
	        	{
	        		if (!f.isCustom())
	        		  fieldNames.add(f.getName());
	        	}
	        }

	        
	        // package
	        write(out,"package " + packageName + ";" + NEWLINE + NEWLINE);
	        
	        write(out, "import org.codehaus.jackson.annotate.JsonProperty;" + NEWLINE);
	        write(out, "import javax.persistence.Column;" + NEWLINE);
	        write(out, "import javax.persistence.GeneratedValue;" + NEWLINE);
	        write(out, "import javax.persistence.GenerationType;" + NEWLINE);
	        write(out, "import javax.persistence.Id;" + NEWLINE);
	        write(out, "import javax.persistence.Entity;" + NEWLINE);
	        write(out, "import javax.persistence.Table;" + NEWLINE);
	        write(out, "import javax.xml.bind.annotation.XmlAttribute;" + NEWLINE);
	        write(out, "import javax.xml.bind.annotation.XmlRootElement;" + NEWLINE);
	        write(out, "import org.apache.commons.lang.builder.ToStringBuilder;" + NEWLINE);
	        write(out, "import org.codehaus.jackson.annotate.JsonIgnoreProperties;" + NEWLINE);
	        
	        // class comment block
	        write(out,"/**" + NEWLINE);
	        write(out," * Generated from information gathered from /services/data/" + apiVersion + 
	                "/sobjects/" + describe.getName() + "/describe" + NEWLINE);
	        write(out," */" + NEWLINE);
	        
	        // class begin
	        write(out, "@Entity" + NEWLINE);
	        write(out, "@XmlRootElement(name = " + "\"" + className + "\"" + ")"  + NEWLINE);
	        write(out, "@Table(name = " + "\"" + className + "\"" + ")" + NEWLINE);
	        write(out, "@JsonIgnoreProperties(ignoreUnknown=true)" + NEWLINE);
	        write(out,"public class " + className + " implements java.io.Serializable {" + NEWLINE);

	        // constants
	        write(out,TAB + "public static boolean CREATEABLE = "); 
	        write(out,describe.isCreateable() ? "true;" : "false;");
	        write(out,NEWLINE);

	        write(out,TAB + "public static boolean DELETABLE = ");
	        write(out,describe.isDeletable() ? "true;" : "false;");
	        write(out,NEWLINE);

	        write(out,TAB + "public static boolean UPDATEABLE = ");
	        write(out,describe.isUpdateable() ? "true;" : "false;");
	        write(out,NEWLINE);
	        
	        write(out,NEWLINE);

	        write(out, TAB + "private Long dbid;" + NEWLINE);
	        
	        // add all private member variables
	        for (Field field : describe.getFields()) 
	        {
	        String fieldNameLower = null;
	        if (customfields)
	        	{
	        	if (field.getName().endsWith("__c"))
	        	{
	        		fieldNameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1,field.getName().length()-3);
	        	}
	        	else
	        	{
	        		fieldNameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1, field.getName().length());
	        	}
	            // write private member
	            write(out,TAB + "@JsonProperty(value=" + "\"" + capitalize(field.getName()) + "\"" + ")" + NEWLINE);
	            write(out,TAB + "private " + getJavaType(field) + " " + fieldNameLower + ";" + NEWLINE);
	        //}
	        	}
	        else{
	        	if (!field.getName().endsWith("__c")){
	        		fieldNameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1, field.getName().length());
	        	}
	        	write(out,TAB + "@JsonProperty(value=" + "\"" + capitalize(field.getName()) + "\"" + ")" + NEWLINE);
		        write(out,TAB + "private " + getJavaType(field) + " " + fieldNameLower + ";" + NEWLINE);
		     }
	        }
	        
	        // no arg constructor
	        write(out,NEWLINE + TAB + "/**" + NEWLINE);
	        write(out,TAB + " * Constructor." + NEWLINE);
	        write(out,TAB + " */" + NEWLINE);
	        write(out,TAB + "public " + className + "() { }" + NEWLINE);
	        
	        // constructor with required fields
	        int count = 0;
	        int max = describe.getFields().size();
	        if(max > count) {
	            
	            write(out,NEWLINE + TAB + "/**" + NEWLINE);
	            write(out,TAB + " * Constructor with required fields." + NEWLINE);
	            write(out,TAB + " */" + NEWLINE);
	            write(out,TAB + "public " + className + "(");
	           
	            for (Field field : describe.getFields()) {
	            	String fieldNameLower = null;
	            	if (customfields)
	            	{
	     
	            		if (field.getName().endsWith("__c"))
	            		{
	            			fieldNameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1,field.getName().length()-3);
	            		}
	            		else
	            		{
	            			fieldNameLower = field.getName().substring(0, 1).toLowerCase()
	                            + field.getName().substring(1, field.getName().length());
	            		}
	                
	                write(out,getJavaType(field) + " " + fieldNameLower);
	                if (++count != max) { write(out,", "); }
	            	}
	            	else
	            	{
	            		if (!field.getName().endsWith("__c")){
	            			fieldNameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1, field.getName().length());
	            		}
	            		 write(out,getJavaType(field) + " " + fieldNameLower);
	 	                if (++count != max) { write(out,", "); }
	            	}
	            }
	            write(out,") {" + NEWLINE);
	            
	            //constructor body
	            for (Field field : describe.getFields()) {
	                String fieldNameLower;
	                
	                if (field.getName().endsWith("__c"))
	                {
	                 	fieldNameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1,field.getName().length()-3);
	                }
	                else
	                {
	             	   fieldNameLower = field.getName().substring(0, 1).toLowerCase()
	                            + field.getName().substring(1, field.getName().length());
	                }
	    
	                write(out,TAB + TAB + "this." + fieldNameLower + " = " + fieldNameLower + ";" + NEWLINE);
	            }
	            write(out,TAB + "}" + NEWLINE + NEWLINE);
	        }

	        String dbId="dbId";
	        write(out, TAB + "@Id" + NEWLINE);
	        write(out, TAB + "@GeneratedValue(strategy = GenerationType.IDENTITY)" + NEWLINE);
	        write(out, TAB + "@Column(name = " + "\"" + dbId + "\"" + ", unique = true, nullable = false)" + NEWLINE);
	        write(out, TAB + "public Long getdbId() {" + NEWLINE);
	        write(out, TAB + TAB + "return dbid;" + NEWLINE);
	        write(out, TAB + "}" + NEWLINE);
	        write(out, TAB + "public void setdbId(Long dbid) {" + NEWLINE);
	        write(out, TAB + TAB + "this.dbid = dbid;" + NEWLINE);
	        write(out, TAB + "}" + NEWLINE);
	        
	        
	        // add getters for every private member variable (no need to hide anything)
	        for (Field field : describe.getFields()) {
	        String fieldNameLower;
	        if (field.getName().endsWith("__c"))
	       {
	        	fieldNameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1,field.getName().length()-3);
	       }
	       else
	       {
	    	   fieldNameLower = field.getName().substring(0, 1).toLowerCase()
	                   + field.getName().substring(1, field.getName().length());
	       }
	            
	            write(out, TAB+ "@XmlAttribute(name = " + "\"" + capitalize(field.getName()) + "\"" + ")" + NEWLINE);
	            write(out, TAB+ "@Column(name = " + "\""+ capitalize(field.getName()) + "\""+ ")" + NEWLINE);
	            write(out,TAB + "public " + getJavaType(field) + " get" + capitalize(field.getName()) + "() {"
	                    + NEWLINE);
	            write(out,TAB + TAB + "return " + fieldNameLower + ";" + NEWLINE);
	            write(out,TAB + "}" + NEWLINE);
	            
	            // for custom fields, add an alias getter without __c unless it conflicts with a standard field name
	         
	          }
	        //}

	        // add setters for all fields
	        for (Field field : describe.getFields()) {
	        String fieldNameUpper = field.getName();
	        String fieldNameLower = null;
	        if (field.getName().endsWith("__c"))
	        {
	        	fieldNameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1,field.getName().length()-3);
	        }
	        else
	        {
	        	 fieldNameLower = field.getName().substring(0, 1).toLowerCase()
	                     + field.getName().substring(1, field.getName().length());
	        }
	        	
	            write(out,TAB+ "/**" + NEWLINE);
	            if ((!field.isNillable()) && (!field.isDefaultedOnCreate())) {
	            	write(out,TAB + " * " + fieldNameUpper + " is a required field." + NEWLINE);
	            } else {
	            	if(field.getRelationshipName() == null) {
	                    write(out,TAB + " * " + fieldNameUpper + " is an optional field." + NEWLINE);
	            	} else {
	                    write(out,TAB + " * " + fieldNameUpper + " is a reference to a parent entity." + NEWLINE);
	            	}
	            }
	            write(out,TAB+ " */" + NEWLINE);
	            write(out,TAB + "public void set" + capitalize(fieldNameUpper) + "(" + getJavaType(field)
	                    + " " + fieldNameLower + ") {" + NEWLINE);
	            write(out,TAB + TAB + "this." + fieldNameLower + " = " + fieldNameLower + ";" + NEWLINE);
	            write(out,TAB + "}" + NEWLINE);

	            // for custom fields, add an alias setter without __c unless it conflicts with a standard field name
	           
	           
	       // }
	        }
	        write(out, NEWLINE);
	        write(out, TAB + "@Override" + NEWLINE);
	        write(out, TAB + "public String toString(){" + NEWLINE);
	        write(out, TAB + "return ToStringBuilder.reflectionToString(this);}" + NEWLINE);

	        // Note: there are no setters for things that can never be set, like Ids and system fields

	        // class end
	        write(out,"}" + NEWLINE);
	       
	        return true;   
	    }
	    
	    
	    /**
	     * This method attempts to match the salesforce sobject field type to Java. If it doesn't know, it returns String.
	     * 
	     * @param salesforceFieldType
	     * @return
	     */
	    private String getJavaType(Field field) {
	        if (field.getType().equals("int")) {
	            return "Integer";
	        } 
	        else if (field.getType().equals("boolean")) {
	            return "Boolean";
	        } 
	        else if (field.getType().equals("double")) {
	            return "Double";
	        } 
	        else {
	            return "String";
	        }
	    }
	    
	    private String capitalize(String in) {
	    	if(Character.isUpperCase(in.indexOf(0))) { 
	    		return in; 
	    	} else {
	    		return new String(Character.toUpperCase(in.charAt(0))+in.substring(1,in.length()));
	    	}
	    	
	    }
	    
	   
	    private static final String username = "username";
	    private static final String password = "password";
	    		
	    public static void main(String[] args) throws IOException {
	    	
	    	ForceApi api = new ForceApi(new ApiConfig()
			.setUsername(username)
			.setPassword(password));
	    	
	    	DescribeSObject ds = api.describeSObject("Account");
	    	
	    	OutputStream output = new FileOutputStream(new File("/Users/thysmichels/Desktop/Model/Account.java"));

	    	ModelGenerator cgs = new ModelGenerator();
	    	cgs.generateCodeStandardAndCustomFields(output, ds, "27.0", "com.example.model", false);
		}
	
}
