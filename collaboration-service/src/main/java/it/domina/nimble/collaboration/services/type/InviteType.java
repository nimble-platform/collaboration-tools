package it.domina.nimble.collaboration.services.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class InviteType extends JsonType {

    private static final Logger logger = Logger.getLogger(InviteType.class);

    private String token;
    private String projectName;
	private String userID;
    
	protected InviteType() {}
	
	public InviteType(String token, String projectName, String userID) {
    	this.token = token;
    	this.projectName = projectName;
    	this.userID = userID;
    }


    public String getToken() {
    	return this.token;
    }

	public String getProjectName() {
    	return this.projectName;
    }

    public String getUserID() {
    	return this.userID;
    }

	public static InviteType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, InviteType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}
}
