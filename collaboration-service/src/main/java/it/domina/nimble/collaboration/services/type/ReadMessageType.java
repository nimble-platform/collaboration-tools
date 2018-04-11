package it.domina.nimble.collaboration.services.type;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class ReadMessageType extends JsonType {

    private static final Logger logger = Logger.getLogger(ReadMessageType.class);

   	private String token;
   	private String projectName;
   	private List<CollabMessageType> messages = new Vector<CollabMessageType>();  
	
    public ReadMessageType() { }
    
    public ReadMessageType(String token, String projectName) { 
    	this.token = token;
    	this.projectName = projectName;
    }

    public String getToken() {
    	return this.token;
    }

    public String getProjectName() {
    	return this.projectName;
    }

    public List<CollabMessageType> getMessages(){
    	return this.messages;
    }

    public static ReadMessageType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ReadMessageType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

	
}
