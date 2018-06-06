package it.domina.nimble.collaboration.services.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class SaveResourceType extends JsonType {

    private static final Logger logger = Logger.getLogger(SaveResourceType.class);

   	private String token;
   	private ResourceType resource;  
	
    public SaveResourceType() { }
    
    public SaveResourceType(String token) { 
    	this(token, null);
    }

    public SaveResourceType(String token, ResourceType res) { 
    	this.token = token;
    	this.resource = res;
    }

    public String getToken() {
    	return this.token;
    }

    public ResourceType getResource(){
    	return this.resource;
    }

    public static SaveResourceType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, SaveResourceType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

	
}
