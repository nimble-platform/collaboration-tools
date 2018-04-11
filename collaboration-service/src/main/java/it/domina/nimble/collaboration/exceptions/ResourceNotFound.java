package it.domina.nimble.collaboration.exceptions;

public class ResourceNotFound  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public static final String ERRCODE = "RESOURCE_NOT_FOUND";
	
	public ResourceNotFound() {
		super(ERRCODE);
	}
	
}
