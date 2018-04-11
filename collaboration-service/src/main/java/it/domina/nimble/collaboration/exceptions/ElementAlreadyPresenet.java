package it.domina.nimble.collaboration.exceptions;

public class ElementAlreadyPresenet  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ERRCODE = "ELEMENT_ALREADY_PRESENT";

	
	public ElementAlreadyPresenet() {
		super(ERRCODE);
	}
	
}
