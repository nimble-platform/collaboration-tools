package it.domina.nimble.collaboration.services.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.collaboration.config.EInvite;
import it.domina.nimble.utils.JsonType;

public class InviteType extends JsonType {

    private static final Logger logger = Logger.getLogger(InviteType.class);

    private String id;
    private String token;
    private String projectName;
	private String user;
	private String userFrom;
    
	protected InviteType() {}

	public InviteType(String token, EInvite inv) {
    	this.token = token;
    	this.projectName = inv.getProject().getName();
    	this.userFrom = inv.getPartnerFrom().getUsername();
    	this.user = inv.getUser();
    	this.id = inv.getID().toString();
    }

	public InviteType(String token, String projectName, String user) {
    	this.token = token;
    	this.projectName = projectName;
    	this.user = user;
    }

    public String getId() {
    	return this.id;
    }

    public String getToken() {
    	return this.token;
    }

	public String getProjectName() {
    	return this.projectName;
    }

    public String getUser() {
    	return this.user;
    }

    public String getUserFrom() {
    	return this.userFrom;
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
