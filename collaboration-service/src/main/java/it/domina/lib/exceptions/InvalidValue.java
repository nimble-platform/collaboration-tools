package it.domina.lib.exceptions;

public class InvalidValue extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidValue(String value){
		super("Invalid value: "+value);
	}
	
}
