package it.domina.lib.exceptions;

public class ItemNotFound extends Exception {
	
	private static final long serialVersionUID = -634668559882624115L;

	public ItemNotFound(String msg){
		super("Item as index " + msg +" not Found");
	}

}
