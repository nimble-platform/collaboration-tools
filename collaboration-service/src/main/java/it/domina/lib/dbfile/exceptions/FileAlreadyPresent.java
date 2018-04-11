package it.domina.lib.dbfile.exceptions;

public class FileAlreadyPresent extends Exception {

	private static final long serialVersionUID = 1L;
	private String name;
	
	public FileAlreadyPresent(String name){
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "ARCHIVE ALREADY PRESENT: "+ this.name;
	}
	
}
