package it.domina.lib.store.exception;

public class LinkedRecord extends Exception {

	private static final long serialVersionUID = 1L;

	public LinkedRecord(String name){
		super("Impossibile Cancellare: "+name+". Trovati dati collegati.");
	}

}
