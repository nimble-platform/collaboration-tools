package it.domina.nimble.collaboration.services.type;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class SaveResourceType extends JsonType {

    private static final Logger logger = Logger.getLogger(SaveResourceType.class);

   	private String token;
   	private List<ResourceType> resources = new Vector<ResourceType>();  
	
    public SaveResourceType() { }
    
    public SaveResourceType(String token) { 
    	this(token, null);
    }

    public SaveResourceType(String token, ResourceType res) { 
    	this.token = token;
    	if (res!=null) {
    		this.resources.add(res);
    	}
    }

    public String getToken() {
    	return this.token;
    }

    public List<ResourceType> getResources(){
    	return this.resources;
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
