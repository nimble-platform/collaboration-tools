package it.domina.lib.store.exception;

public class RecordNotFound extends Exception {

	private static final long serialVersionUID = 1L;

	/*
	public RecordNotFound(){
		super("Record not found");
	}
	*/
	
	public RecordNotFound(String name){
		super("Record not found:"+name);
	}

}
