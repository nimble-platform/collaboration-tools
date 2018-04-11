package it.domina.lib.dbfile.exceptions;

public class FileNotFound extends Exception {

	private static final long serialVersionUID = 1L;
	private String name;
	
	public FileNotFound(String name){
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "FILE NOT FOUND: "+ this.name;
	}
	
}
