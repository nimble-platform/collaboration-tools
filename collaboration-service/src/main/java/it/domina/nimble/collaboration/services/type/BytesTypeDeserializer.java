package it.domina.nimble.collaboration.services.type;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class BytesTypeDeserializer extends StdDeserializer<BytesType> {

    private static final long serialVersionUID = 1514703510863497028L;

    public BytesTypeDeserializer() {
        super(BytesType.class);
    }

    @Override
    public BytesType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        String base64 = node.asText();
        return new BytesType(Base64.getDecoder().decode(base64));
    }
}
