package it.domina.lib.dbfile.exceptions;

public class PathNotFound extends Exception {

	private static final long serialVersionUID = 1L;
	private String name;
	
	public PathNotFound(String name){
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "PATH NOT FOUND: "+ this.name;
	}
	
}
