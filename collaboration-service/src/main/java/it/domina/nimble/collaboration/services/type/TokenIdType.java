package it.domina.nimble.collaboration.services.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class TokenIdType extends JsonType {

    private static final Logger logger = Logger.getLogger(TokenIdType.class);

   	private String token;
	
   	protected TokenIdType() {}

    public TokenIdType(String token) {
    	this.token = token;
    }

    public String getToken() {
    	return this.token;
    }

	public static TokenIdType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, TokenIdType.class);
            //return (new Gson()).fromJson(json, TokenIdType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

}
