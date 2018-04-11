package it.domina.lib.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class InputParams implements ProcedureParams {

	private String	callSQL;
	private Boolean query;
	private List<String> columns;// = new HashMap<String, String>();
	private List<Object> params;
	private HashMap<Integer,Boolean> outputMode = new HashMap<Integer, Boolean>();
	
	public InputParams(String call, Boolean qry){
		this.callSQL = call;
		this.query = qry;
		this.columns  = new Vector<String>();
		this.params = new ArrayList<Object>();
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
		addParam(value, false);
	}

	public void addParam(Object value, Boolean output){
		this.params.add(value);
		if (output){
			this.outputMode.put(this.params.indexOf(value),output);
		}
	}

	@Override
	public Boolean isOutputParameter(String name){
		return false;
	}
	
	@Override
	public List<String> getColumnsName() {
		return this.columns;
	}
	
	@Override
	public List<Object> getParams() {
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

}
