package it.domina.nimble.collaboration.auth.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import it.domina.nimble.utils.JsonType;

public class CredentialType extends JsonType {

	private static final Logger logger = Logger.getLogger(CredentialType	.class);

   	private String username;
   	private String password;
	
    public CredentialType(String username, String password) {
    	this.username = username;
    	this.password = password;
    }

    public String getUsername() {
    	return this.username;
    }

    public String getPassword() {
    	return this.password;
    }

	public static CredentialType mapJson(String json) {
		try {
            return (new Gson()).fromJson(json, CredentialType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

}
