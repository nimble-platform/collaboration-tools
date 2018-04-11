package it.domina.nimble.collaboration.services;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import it.domina.nimble.collaboration.ServiceConfig;

/**
 * Servlet implementation class ConfigLoader
 */
@WebServlet(name = "configLoader", urlPatterns = { "/configLoader" }, loadOnStartup=7 )
public class ConfigLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		ServiceConfig.create(config.getServletContext().getRealPath(""));
	}

}
