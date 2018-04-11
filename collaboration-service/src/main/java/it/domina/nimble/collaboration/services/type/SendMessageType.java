package it.domina.nimble.collaboration.services.type;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class SendMessageType extends JsonType {

    private static final Logger logger = Logger.getLogger(SendMessageType.class);

   	private String token;
   	private List<CollabMessageType> messages = new Vector<CollabMessageType>();  
	
    public SendMessageType() { }
    
    public SendMessageType(String token) { 
    	this(token, null);
    }

    public SendMessageType(String token, CollabMessageType msg) { 
    	this.token = token;
    	if (msg!=null) {
    		this.messages.add(msg);
    	}
    }

    public String getToken() {
    	return this.token;
    }

    public List<CollabMessageType> getMessages(){
    	return this.messages;
    }

    public static SendMessageType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, SendMessageType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

	
}
