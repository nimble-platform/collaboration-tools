package it.domina.lib.store.exception;

public class DuplicateRecord extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateRecord(String pk){
		super("The Record "+ pk +" is present");
	}
	
}
