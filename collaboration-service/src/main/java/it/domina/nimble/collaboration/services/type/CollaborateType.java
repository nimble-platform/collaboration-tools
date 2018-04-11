package it.domina.nimble.collaboration.services.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class CollaborateType extends JsonType {

    private static final Logger logger = Logger.getLogger(CollaborateType.class);

	private String token;
    private String projectName;
    private String url;
	
    public CollaborateType() {
    	
    }
    
    public CollaborateType(String token, String projectName, String url) {
    	this.token = token;
    	this.url = url;
    	this.projectName = projectName;
    }
    
    public String getToken() {
    	return this.token;
    }

    public String getUrl() {
    	return this.url;
    }

    public String getProjectName() {
    	return this.projectName;
    }

	public static CollaborateType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, CollaborateType.class);
            //return (new Gson()).fromJson(json, CollaborateType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

}
