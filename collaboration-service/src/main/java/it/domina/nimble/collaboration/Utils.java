package it.domina.nimble.collaboration;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

public class Utils {

    public static String getInputStreamAsString(InputStream stream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");
        return writer.toString();
    }

}
