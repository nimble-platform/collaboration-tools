package it.domina.nimble.collaboration.services;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.domina.nimble.collaboration.Utils;

/**
 * Servlet implementation class HelloWord
 */
@WebServlet(name = "Echo", urlPatterns = { "/echo" })
public class Echo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Echo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responseString = Utils.getInputStreamAsString(request.getInputStream());
		response.getWriter().append(responseString);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responseString = Utils.getInputStreamAsString(request.getInputStream());
		response.getWriter().append(responseString);
	}

}
