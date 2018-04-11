package it.domina.lib.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ProcedureParametes implements StoreProcedureParams {

	private String	callSQL;
	private Boolean query;
	private List<String> columns;// = new HashMap<String, String>();
	private List<ProcedureParameterField> params;
	
	public ProcedureParametes(String call, Boolean qry){
		this.callSQL = call;
		this.query = qry;
		this.columns  = new Vector<String>();
		this.params = new ArrayList<ProcedureParameterField>();
	}

	public void clearColumnNames(){
		this.columns.clear();
	}

	public void addColumnNames(String name){
		this.columns.add(name);
	}
	
	public void clearParams(){
		this.params.clear();
	}
	
	public void addParam(Object value){
		this.params.add(new ProcedureParameterField(value));
	}

	public void addOutputParam(String name, FieldType tipo){
		this.params.add(new ProcedureParameterField(name,tipo,true));
	}

	@Override
	public List<String> getColumnsName() {
		return this.columns;
	}
	
	@Override
	public List<ProcedureParameterField> getParams() {
		return this.params;
	}

	@Override
	public Boolean isQuery(){
		return this.query;
	}

	@Override
	public String getSQLCall(){
		return this.callSQL;
	}

	@Override
	public Boolean isOutputParameter(String name) {
		for (ProcedureParameterField prf : this.params){
			if (prf.getName().equals(name)){
				return prf.isOutput();
			}
		}
		return false;
	}

}
