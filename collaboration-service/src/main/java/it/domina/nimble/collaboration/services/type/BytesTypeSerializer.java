package it.domina.nimble.collaboration.services.type;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class BytesTypeSerializer extends StdSerializer<BytesType> {

    private static final long serialVersionUID = -5510353102817291511L;

    public BytesTypeSerializer() {
        super(BytesType.class);
    }

    @Override
    public void serialize(BytesType value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(new String (Base64.getEncoder().encode(value.getBytes())));
    }
}