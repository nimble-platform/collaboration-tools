package it.domina.lib.exceptions;

public interface DFError {

	public static Integer WARNING = 1;
	public static Integer ERROR = 2;
	
	public String getCode();
	public String getDescription();
	public Integer getType();
	
}
