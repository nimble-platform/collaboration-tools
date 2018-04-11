package it.domina.lib.dbfile.exceptions;

public class FolderNotFound extends Exception {

	private static final long serialVersionUID = 1L;
	private String name;
	
	public FolderNotFound(String name){
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "ARCHIVE NOT FOUND: "+ this.name;
	}
	
}
