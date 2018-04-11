package it.domina.nimble.collaboration.exceptions;

public class SubscriptionRequired  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public static final String ERRCODE = "SUBSCRIPTION_REQUIRED";
	
	public SubscriptionRequired() {
		super(ERRCODE);
	}
	
}
