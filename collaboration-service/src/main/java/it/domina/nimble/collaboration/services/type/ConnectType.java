package it.domina.nimble.collaboration.services.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class ConnectType extends JsonType {

    private static final Logger logger = Logger.getLogger(ConnectType.class);

    private String token;
    private String inviteID;
    
	protected ConnectType() {}
	
	/*
	public InviteType(EProject prj) {
		this.id = prj.getID().toString();
    	this.name = prj.getName();
    	this.owner = prj.getOwner().getUserId();
    	this.token = "";
    }
	*/
	
	public ConnectType(String token, String inviteID) {
    	this.token = token;
    	this.inviteID = inviteID;
    }

    public String getToken() {
    	return this.token;
    }

	public String getInviteID() {
    	return this.inviteID;
    }


	public static ConnectType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ConnectType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}
}
