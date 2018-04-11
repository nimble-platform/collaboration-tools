package it.domina.lib.exceptions.impl;

import it.domina.lib.exceptions.DFError;

public abstract class GenericError implements DFError {

	private String  code;
	private String  descrizione;
	private Integer tipo;

	protected GenericError(String code, String descrizione, Integer tipo){
		this.code = code;
		this.descrizione = descrizione;
		this.tipo = tipo;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getDescription() {
		return this.descrizione;
	}

	@Override
	public Integer getType() {
		return this.tipo;
	}

	
	
}
