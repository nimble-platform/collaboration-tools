package it.domina.nimble.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HTTPUtils_ {

    public static CloseableHttpResponse sendPostCommand(String url, String content, Header... headers) {
        CloseableHttpResponse response = null;

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            for (Header h : headers) {
                httpPost.setHeader(h);
            }
            HttpEntity entity = new ByteArrayEntity(content.getBytes("UTF-8"));
            httpPost.setEntity(entity);

            response = httpclient.execute(httpPost);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    public static void closeResponse(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getKeyFromJsonString(String key, String jsonString) {
	    JsonObject jsonOut = (JsonObject) new JsonParser().parse(jsonString);
	    if (jsonOut != null) {
	        return jsonOut.get(key).getAsString();
	    }
	    throw new RuntimeException(String.format("Missing key '%s' in json string '%s'", key, jsonString));
	}

    public static String getInputStreamAsString(InputStream stream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");
        return writer.toString();
    }

    public static String getPostAsString(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
	    String s;
	    while ((s = request.getReader().readLine()) != null) {
	        sb.append(s);
	    }
	    return sb.toString();
    }
    
    public static JsonObject readJsonParam(HttpServletRequest request) throws IOException {
    	String pars = getPostAsString(request);
	    JsonObject jsonOut = (JsonObject) new JsonParser().parse(pars);
        return jsonOut;
    }

    	
}
