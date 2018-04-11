package it.domina.lib.store.exception;

public class ArchiveNotFound extends Exception {

	private static final long serialVersionUID = 1L;
	private String name;
	
	public ArchiveNotFound(String name){
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "ARCHIVE NOT FOUND: "+ this.name;
	}
	
}
