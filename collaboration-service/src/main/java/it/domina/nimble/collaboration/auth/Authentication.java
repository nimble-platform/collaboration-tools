package it.domina.nimble.collaboration.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.nimble.collaboration.auth.type.CredentialType;
import it.domina.nimble.collaboration.auth.type.IdentityUserType;
import it.domina.nimble.collaboration.core.Session;

public class Authentication {

	private static String BASE_URL = "https://nimble-platform.salzburgresearch.at/nimble/identity";
	
	private static final Logger logger = Logger.getLogger(Authentication.class);
	
	private Boolean forceGranted;
	
	private HashMap<String, IdentityUserType> authenticateUsers;
	//private HashMap<String, Session> sessions;
	
	public Authentication(String userdir) {
		
		String propfile = userdir + File.separator + "auth.properties";
		Properties prop = loadConfiguration(propfile);
		if (prop!=null) {
	        if (prop.getProperty("forceGranted").equals("true")) {
				this.forceGranted = true;
				this.authenticateUsers = new HashMap<String, IdentityUserType>();
			}
			else {
				this.forceGranted = false;
			}
	        //this.sessions = new HashMap<String, Session>();
			logger.log(Level.INFO, "Auth initialize");
		}
	}
	
	public Session getPermission(String token, String action) {
		IdentityUserType user = getUserDetail(token); 
		if (user!=null) {
			if (this.forceGranted) {
				return new Session(user);
			}
			else {
				//TODO:  connect to identity service for permission
				return null;
			}
		}
		return null;
	}
	
	public IdentityUserType	userLogin(String username, String password) {
        CloseableHttpResponse response = null;
        try {
        	CredentialType params = new CredentialType(username, password);
            response = sendPostCommand("/login", params.toString());
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.log(Level.INFO, responseString);
            IdentityUserType u = IdentityUserType.mapJson(responseString);
            if (u!= null) {
	            if (this.forceGranted) {
	            	this.authenticateUsers.put(u.getAccessToken(), u);
	            }
	            return u;
            }
            else {
            	return null;
            }
        } catch (Exception ex){
            logger.log(Level.ERROR, ex);
        } finally {
        	closeResponse(response);
        }
        return null;
	}

	public IdentityUserType getUserDetail(String token) {
		if (this.forceGranted) {
			return this.authenticateUsers.get(token);
		}
		else{
			return null;
		}
	}
	
    private Properties loadConfiguration( String fileName) {
        InputStream propsStream;
        try {
        	Properties applicationProperties = new Properties();
            propsStream = new FileInputStream(fileName);
            applicationProperties.load(propsStream);
            propsStream.close();
            return applicationProperties;
        } catch (IOException e) {
            logger.log(Level.ERROR, e);
        }
        return null;
    }
    
    private CloseableHttpResponse sendPostCommand(String url, String content, Header... headers) {
        CloseableHttpResponse response = null;

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(BASE_URL + url);
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

    private void closeResponse(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    public static String logAndGetResponse(HttpResponse response) {
        try {
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.log(Level.INFO, String.format("Status=%s, Response=%s", response.getStatusLine().toString(), responseString));
            return responseString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    */
    
}
