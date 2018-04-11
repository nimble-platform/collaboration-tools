package it.domina.lib.store;


public class ProcedureParameterField {

	private String 		name;
	private FieldType 	tipo;
	private Boolean 	output;
	private Object 		value;
	
	public ProcedureParameterField(Object val){
		this.value = val;
		this.output = false;
	}

	public ProcedureParameterField(String n, FieldType ty, Boolean out){
		this.name = n;
		this.tipo = ty;
		this.output = out;
	}

	public String getName(){
		return this.name;
	}
	
	public Boolean isOutput(){
		return this.output;
	}

	public FieldType getTipo(){
		return this.tipo;
	}

	public Object getValue(){
		return this.value;
	}

	public void setValue(Object o){
		this.value = o;
	}

}
