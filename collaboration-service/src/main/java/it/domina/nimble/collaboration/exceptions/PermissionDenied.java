package it.domina.nimble.collaboration.exceptions;

public class PermissionDenied  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ERRCODE = "PERMISSION_DENIED";

	
	public PermissionDenied() {
		super(ERRCODE);
	}
}
