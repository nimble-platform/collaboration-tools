package it.domina.nimble.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonType {
	
    private static final Logger logger = Logger.getLogger(JsonType.class);


	@Override
    public String toString() {
        try {
        	ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (final JsonProcessingException e) {
			logger.log(Level.INFO, e.getStackTrace());
            return "";
        }
    }


}
