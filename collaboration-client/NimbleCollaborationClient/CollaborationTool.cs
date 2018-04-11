using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.IO;
using System.Diagnostics;
using System.Web.Script.Serialization;
using Nimble.Client.Type;
using Nimble.Client.Exceptions;

namespace Nimble.Client
{
    public class CollaborationTool
    {

        public static string baseURL = "http://localhost:8081/collaboration-service";

        public static String Echo(String text)
        {
            return sendPostCommand("/echo", text);
        }

        public static String login(String name, String pwd) {
            try {
            	CredentialType par = new CredentialType(name, pwd);
                return sendPostCommand("/login", par.ToString());
            } catch (Exception ex){
                logError(ex);
            }
            return null;
        }

        public static Boolean subscribe(String tokenid) {
            try{
        	    TokenIdType par = new TokenIdType(tokenid);
                String response = sendPostCommand("/subscribe", par.ToString());
                if (response!=null) {
        	        return true;
                }
            }
            catch(Exception ex){
                logError(ex);
            }
            return false;
        }

        public static List<ProjectType> getProjectList(String tokenid) {
            try{
        	    TokenIdType par = new TokenIdType(tokenid);
                String response = sendPostCommand("/projectList", par.ToString());
                if (response!=null) {
                    if (response.Equals(SubscriptionRequired.ERRCODE))
                    {
                        throw new SubscriptionRequired();
                    }
                    else
                    {
                        ProjectListType lstProject = ProjectListType.mapJson(response);
                        return lstProject.getProjectList();
                    }
                }
            }
            catch(Exception ex){
                logError(ex);
            }
            return null;
        }

        public static Boolean createProject(String tokenid, String name) {
            try {
                ProjectType par = new ProjectType(tokenid, name);
                String response = sendPostCommand("/createProject", par.ToString());
                if (response != null)
                {
                    return true;
                }
                else {
            	    return false;
                }
            } catch (Exception ex){
                logError(ex);
            }
            return false;
        }

        public static String sendInvite(String tokenid, String projectName, String userid) {
            try {
                InviteType par = new InviteType(tokenid, projectName, userid);
                String response = sendPostCommand("/sendInvite", par.ToString());
                if (response != null){
                    return response;
                }
            } catch (Exception ex){
                logError(ex);
            }
            return null;
        }
        
        public static String connectProject(String tokenid, String inviteID) {
            try {
        	    ConnectType par = new ConnectType(tokenid, inviteID);
                String response = sendPostCommand("/connectProject", par.ToString());
                if (response != null)
                {
                    return response;
                }
                else {
            	    return null;
                }
            } catch (Exception ex){
                logError(ex);
            }
            return null;
        }

        public static Boolean startCollaborate(String tokenid, String projectName) {
            try {
        	    CollaborateType par = new CollaborateType(tokenid, projectName, null);
                String response = sendPostCommand("/startCollaboration", par.ToString());
                if (response != null)
                {
            	    return true;
                }
            } catch (Exception ex){
                logError(ex);
            }
            return false;
        }

        public static void sendMessage(SendMessageType msg)
        {
            try
            {
                sendPostCommand("/sendMessage", msg.ToString());
            }
            catch (Exception ex)
            {
                logError(ex);
            }
        }

        public static ReadMessageType readMessages(String token, String projectName) {
            try {
        	    ReadMessageType par = new ReadMessageType(token, projectName);
                String response = sendPostCommand("/readMessages", par.ToString());
                if (response!=null)
                {
                    ReadMessageType msgList = ReadMessageType.mapJson(response);
                    return msgList;
                }
                else {
                    if (response.Equals(SubscriptionRequired.ERRCODE))
                    {
            		    throw new SubscriptionRequired();
            	    }
                }
            } catch (Exception ex){
                logError(ex);
            }
            return null;
        }

        public static void saveResources(SaveResourceType res)
        {
            try
            {
                String response = sendPostCommand("/saveResource", res.ToString());
            }
            catch (Exception ex)
            {
                logError(ex);
            }
        }
        
        public static ResourceListType getResourceList(String tokenId, String projectName, String name) {
            try {
                ResourceListType par = new ResourceListType(tokenId, projectName, name);
                String response = sendPostCommand("/resourceList", par.ToString());
                if (response != null) {
                    return ResourceListType.mapJson(response);
                }
                else {
                    if (response.Equals(SubscriptionRequired.ERRCODE))
                    {
        		        throw new SubscriptionRequired();
        	        }
                }
            } catch (Exception ex){
                logError(ex);
            }
            return null;
        }

        public static ResourceType getResources(String token, String project, String resourceName)
        {
            return getResources(token, project, resourceName, null);
        }

        public static ResourceType getResources(String token, String project, String resourceName, int? ver)
        {
            try {
        	    ReadResourceType par = new ReadResourceType(token, project, resourceName, ver);
                String response = sendPostCommand("/getResource", par.ToString());
                if (response != null)
                {
                    ResourceType res = ResourceType.mapJson(response);
                    return res;
                }
                else {
                    if (response.Equals(SubscriptionRequired.ERRCODE))
                    {
            		    throw new SubscriptionRequired();
            	    }
                }
            } catch (Exception ex){
                logError(ex);
            }
            return null;
        }

        private static String sendPostCommand(String url, String data)
        {
            try
            {
                HttpWebRequest POSTRequest = (HttpWebRequest)WebRequest.Create(baseURL + url);
                POSTRequest.Method = "POST";
                byte[] postDataBytes = Encoding.UTF8.GetBytes(data);
                POSTRequest.ContentType = "application/json";
                POSTRequest.ContentLength = postDataBytes.Length;
                POSTRequest.KeepAlive = false;
                POSTRequest.Timeout = 5000;

                Stream POSTstream = POSTRequest.GetRequestStream();
                POSTstream.Write(postDataBytes, 0, postDataBytes.Length);
                POSTstream.Close();
                HttpWebResponse POSTResponse = (HttpWebResponse)POSTRequest.GetResponse();
                StreamReader reader = new StreamReader(POSTResponse.GetResponseStream(), Encoding.UTF8);
                return reader.ReadToEnd().ToString();
            }
            catch(Exception ex)
            {
                logError(ex);
            }
            return null;
        }

        private static void logError(Exception ex){
            string ls_Out = ex.StackTrace;
            Trace.WriteLine(ex.Message + "\n Trace=" + ls_Out);
        }


    }

}

