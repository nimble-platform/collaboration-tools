package it.domina.nimble.collaboration.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.nimble.collaboration.ServiceConfig;
import it.domina.nimble.collaboration.Utils;
import it.domina.nimble.collaboration.config.EInvite;
import it.domina.nimble.collaboration.core.Session;
import it.domina.nimble.collaboration.exceptions.PermissionDenied;
import it.domina.nimble.collaboration.services.type.InviteListType;
import it.domina.nimble.collaboration.services.type.TokenIdType;

@WebServlet(name = "inviteList", description = "Get the invite list for the account", urlPatterns = { "/inviteList" })
public class InviteList extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(InviteList.class);
       
	/**
	 * Create a new project and save the user as onwer of it
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String content = request.getHeader("content-type");
	        if (content.equals("application/json")) {
	            String responseString = Utils.getInputStreamAsString(request.getInputStream());
	            logger.log(Level.INFO, responseString);
	            TokenIdType params = TokenIdType.mapJson(responseString);
	            Session sess = ServiceConfig.getInstance().getAuth().getPermission(params.getToken(), "inviteList");
	            if (sess!=null) {
	            	List<EInvite> lst = ServiceConfig.getInstance().getConfig().getInvites(sess);
		    		InviteListType lstInv = new InviteListType(params.getToken(), lst);
	            	response.getWriter().write(lstInv.toString());
		    		response.setStatus(HttpServletResponse.SC_OK);
	            }
		        else {
		        	logger.log(Level.INFO, PermissionDenied.ERRCODE);
		            response.getWriter().write(PermissionDenied.ERRCODE);
		            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		        }
	        }
	        else {
	            String err = "The content-type should application/json";
	        	logger.log(Level.INFO, err);
	            response.getWriter().write(err);
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        }
		} catch (Exception e) {
        	logger.log(Level.INFO, e);
            response.getWriter().write(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
}
