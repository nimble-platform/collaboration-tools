package it.domina.nimble.collaboration.exceptions;

public class ProjectNotFound  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public static final String ERRCODE = "PROJECT_NOT_FOUND";
	
	public ProjectNotFound() {
		super(ERRCODE);
	}
	
}
