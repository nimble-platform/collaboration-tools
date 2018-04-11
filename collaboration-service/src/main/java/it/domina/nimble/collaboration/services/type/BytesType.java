package it.domina.nimble.collaboration.services.type;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = BytesTypeSerializer.class)
@JsonDeserialize(using = BytesTypeDeserializer.class)

public class BytesType  {
	
    private byte[] bytes;

    public BytesType(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
     
    
	@Override
    public String toString() {
        try {
        	ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (final JsonProcessingException e) {
			//logger.log(Level.INFO, e.getStackTrace());
            return "";
        }
    }

}