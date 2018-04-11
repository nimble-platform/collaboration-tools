package it.domina.lib.store;

import java.util.List;

public interface StoreProcedureParams {

	public List<String> getColumnsName();
	public Boolean isQuery();
	public String getSQLCall();
	public List<ProcedureParameterField> getParams();
	public Boolean isOutputParameter(String name);
	
}
