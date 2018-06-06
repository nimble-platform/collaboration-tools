package clientTest.http;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.nimble.collaboration.auth.type.CredentialType;
import it.domina.nimble.collaboration.exceptions.SubscriptionRequired;
import it.domina.nimble.collaboration.services.type.CollaborateType;
import it.domina.nimble.collaboration.services.type.ConnectType;
import it.domina.nimble.collaboration.services.type.InviteType;
import it.domina.nimble.collaboration.services.type.ProjectListType;
import it.domina.nimble.collaboration.services.type.ProjectType;
import it.domina.nimble.collaboration.services.type.ReadMessageType;
import it.domina.nimble.collaboration.services.type.ReadResourceType;
import it.domina.nimble.collaboration.services.type.ResourceListType;
import it.domina.nimble.collaboration.services.type.ResourceType;
import it.domina.nimble.collaboration.services.type.SaveResourceType;
import it.domina.nimble.collaboration.services.type.SendMessageType;
import it.domina.nimble.collaboration.services.type.TokenIdType;

public class Functions {
	
	private static String BASE_URL = "http://localhost:8081/collaboration-service";
	
    private static final Logger logger = Logger.getLogger(Functions.class);

    public static String login(String name, String pwd) {
        CloseableHttpResponse response = null;
        try {
        	CredentialType params = new CredentialType(name, pwd);
            response = sendPostCommand("/login", params.toString());
            String token = logAndGetResponse(response);
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
            	return token;
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

    public static Boolean subscribe(String tokenid) {
        CloseableHttpResponse response = null;
    	TokenIdType params = new TokenIdType(tokenid);
        response = sendPostCommand("/subscribe", params.toString());
        logAndGetResponse(response);
        if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
        	return true;
        }
        return false;
    }

    public static List<ProjectType> getProjectList(String tokenid) throws Exception {
        CloseableHttpResponse response = null;
    	TokenIdType params = new TokenIdType(tokenid);
        response = sendPostCommand("/projectList", params.toString());
        String strData = logAndGetResponse(response);
        if (response.getStatusLine().getStatusCode()==200) {
            ProjectListType lstProject = ProjectListType.mapJson(strData);
            return lstProject.getProjectList();
        }
        else {
        	if (strData.equals(SubscriptionRequired.ERRCODE)) {
        		throw new SubscriptionRequired();
        	}
        }
    	closeResponse(response);
        return null;
    }

    public static Boolean createProject(String tokenid, String name) {
        CloseableHttpResponse response = null;
        try {
            ProjectType params = new ProjectType(tokenid, name);
            response = sendPostCommand("/createProject", params.toString());
            logAndGetResponse(response);
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
            	return true;
            }
            else {
            	return false;
            }
        } catch (Exception ex){
            logger.log(Level.ERROR, ex);
        } finally {
        	closeResponse(response);
        }
        return false;
    }
    
    public static String sendInvite(String tokenid, String projectName, String userid) {
        CloseableHttpResponse response = null;
        try {
        	InviteType params = new InviteType(tokenid, projectName, userid);
            response = sendPostCommand("/sendInvite", params.toString());
            String inviteId = logAndGetResponse(response);
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
            	return inviteId;
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

    
    public static String connectProject(String tokenid, String inviteID) {
        CloseableHttpResponse response = null;
        try {
        	ConnectType params = new ConnectType(tokenid, inviteID);
            response = sendPostCommand("/connectProject", params.toString());
            String projectName = logAndGetResponse(response);
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
            	return projectName;
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

    public static Boolean startCollaborate(String tokenid, String projectName) {
        CloseableHttpResponse response = null;
        try {
        	CollaborateType params = new CollaborateType(tokenid, projectName, null);
            response = sendPostCommand("/startCollaboration", params.toString());
            logAndGetResponse(response);
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
            	return true;
            }
        } catch (Exception ex){
            logger.log(Level.ERROR, ex);
        } finally {
        	closeResponse(response);
        }
        return false;
    }
    
    public static void sendMessage(SendMessageType msg) {
        CloseableHttpResponse response = null;
        try {
            response = sendPostCommand("/sendMessage", msg.toString());
            logAndGetResponse(response);
            
        } catch (Exception ex){
            logger.log(Level.ERROR, ex);
        } finally {
        	closeResponse(response);
        }
    }
    
    public static ReadMessageType readMessages(String token, String projectName) {
        CloseableHttpResponse response = null;
        try {
        	ReadMessageType params = new ReadMessageType(token, projectName);
            response = sendPostCommand("/readMessages", params.toString());
            String strData = logAndGetResponse(response);
            if (response.getStatusLine().getStatusCode()==200) {
            	ReadMessageType msgList = ReadMessageType.mapJson(strData);
                return msgList;
            }
            else {
            	if (strData.equals(SubscriptionRequired.ERRCODE)) {
            		throw new SubscriptionRequired();
            	}
            }
        	closeResponse(response);
            
        } catch (Exception ex){
            logger.log(Level.ERROR, ex);
        } finally {
        	closeResponse(response);
        }
        return null;
    }
    

    public static void saveResources(SaveResourceType res) {
        CloseableHttpResponse response = null;
        try {
            response = sendPostCommand("/saveResource", res.toString());
            logAndGetResponse(response);
            
        } catch (Exception ex){
            logger.log(Level.ERROR, ex);
        } finally {
        	closeResponse(response);
        }
    }

    public static ResourceListType getResourceList(String tokenId, String projectName) throws Exception {
        CloseableHttpResponse response = null;
        ResourceListType params = new ResourceListType(tokenId, projectName);
        response = sendPostCommand("/resourceList", params.toString());
        String strData = logAndGetResponse(response);
        if (response.getStatusLine().getStatusCode()==200) {
            return ResourceListType.mapJson(strData);
        }
        else {
        	if (strData.equals(SubscriptionRequired.ERRCODE)) {
        		throw new SubscriptionRequired();
        	}
        }
    	closeResponse(response);
        return null;
    }

    public static ResourceListType getResourceHistory(String tokenId, String projectName, String resourceName) throws Exception {
        CloseableHttpResponse response = null;
        ResourceListType params = new ResourceListType(tokenId, projectName, resourceName);
        response = sendPostCommand("/resourceHistory", params.toString());
        String strData = logAndGetResponse(response);
        if (response.getStatusLine().getStatusCode()==200) {
            return ResourceListType.mapJson(strData);
        }
        else {
        	if (strData.equals(SubscriptionRequired.ERRCODE)) {
        		throw new SubscriptionRequired();
        	}
        }
    	closeResponse(response);
        return null;
    }

    public static ResourceType getResource(String token, String project, String resourceName) {
    	return getResource(token, project, resourceName, null);
    }
    
    public static ResourceType getResource(String token, String project, String resourceName, Integer ver) {
        CloseableHttpResponse response = null;
        try {
        	ReadResourceType params = new ReadResourceType(token, project, resourceName, ver);
            response = sendPostCommand("/getResource", params.toString());
            String strData = logAndGetResponse(response);
            if (response.getStatusLine().getStatusCode()==200) {
            	ResourceType res = ResourceType.mapJson(strData);
                return res;
            }
            else {
            	if (strData.equals(SubscriptionRequired.ERRCODE)) {
            		throw new SubscriptionRequired();
            	}
            }
        	closeResponse(response);
            
        } catch (Exception ex){
            logger.log(Level.ERROR, ex);
        } finally {
        	closeResponse(response);
        }
        return null;
    }
    
    public static ResourceType deleteResource(String token, String project, String resourceName) {
    	return deleteResource(token, project, resourceName, null);
    }

    public static ResourceType deleteResource(String token, String project, String resourceName, Integer ver) {
        CloseableHttpResponse response = null;
        try {
        	ReadResourceType params = new ReadResourceType(token, project, resourceName, ver);
            response = sendPostCommand("/deleteResource", params.toString());
            String strData = logAndGetResponse(response);
            if (response.getStatusLine().getStatusCode()==200) {
            	ResourceType res = ResourceType.mapJson(strData);
                return res;
            }
            else {
            	if (strData.equals(SubscriptionRequired.ERRCODE)) {
            		throw new SubscriptionRequired();
            	}
            }
        	closeResponse(response);
            
        } catch (Exception ex){
            logger.log(Level.ERROR, ex);
        } finally {
        	closeResponse(response);
        }
        return null;
    }
    

    private static CloseableHttpResponse sendPostCommand(String url, String content, Header... headers) {
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

    private static void closeResponse(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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


}
