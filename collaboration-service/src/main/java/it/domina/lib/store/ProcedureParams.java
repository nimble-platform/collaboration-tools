package it.domina.lib.store;

import java.util.List;

public interface ProcedureParams {

	public List<String> getColumnsName();
	public Boolean isQuery();
	public String getSQLCall();
	public List<Object> getParams();
	public Boolean isOutputParameter(String name);
	
}
