package it.domina.nimble.collaboration.services.type;

import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class CollabMessageType extends JsonType {

	private static final Logger logger = Logger.getLogger(CollabMessageType.class);

   	private String uniqueID;
   	private String projectName;
   	private String title;
   	private HashMap<String, String> contents = new HashMap<String, String>();  
	
    public CollabMessageType() {
    	this.uniqueID = UUID.randomUUID().toString();
    }
    
    public CollabMessageType(String projectName) { 
    	this(projectName, null);
    }

    public CollabMessageType(String projectName, String title) { 
    	this();
    	this.projectName = projectName;
    	this.title = title;
    }

    public String getUniqueID() {
    	return this.uniqueID;
    }

    public String getProjectName() {
    	return this.projectName;
    }

    public String getTitle() {
    	return this.title;
    }

    public HashMap<String, String> getContents(){
    	return this.contents;
    }
    
    public void setContent(String key, String value) {
    	this.contents.put(key, value);
    }

    public String encodeContext() {
    	String output = null;
    	for (String key : this.contents.keySet()) {
			if (output== null) {
	    		output += key + "=" +  this.contents.get(key);
			}
			else {
	    		output += ";" + key + "=" + this.contents.get(key);
			}
		}
    	return output;
    }
    
    public static CollabMessageType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, CollabMessageType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}


}
