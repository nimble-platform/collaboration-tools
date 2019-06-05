package it.domina.nimble.collaboration.auth.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import it.domina.nimble.utils.JsonType;

public class EmailType extends JsonType  {

	private static final Logger logger = Logger.getLogger(EmailType.class);

	
	private String email;
	
	public String getEmail() {
		return email;
	}

	public static EmailType mapJson(String json) {
		try {
            return (new Gson()).fromJson(json, EmailType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

}

