package it.domina.nimble.collaboration.services.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class ReadResourceType extends JsonType {

    private static final Logger logger = Logger.getLogger(ReadResourceType.class);

   	private String token;
   	private String projectName;
   	private String resourceName;
   	private Integer resourceVersion;
	
    public ReadResourceType() { }
    
    public ReadResourceType(String token, String projectID, String name) { 
    	this(token, projectID, name, null);
    }

    public ReadResourceType(String token, String projectName, String name, Integer ver) { 
    	this.token = token;
    	this.projectName = projectName;
    	this.resourceName = name;
    	this.resourceVersion = ver;
    }

    public String getToken() {
    	return this.token;
    }

    public String getProjectName() {
    	return this.projectName;
    }

    public String getResourceName(){
    	return this.resourceName;
    }

    public Integer getResourceVersion(){
    	return this.resourceVersion;
    }
    
    public static ReadResourceType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ReadResourceType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

	
}
