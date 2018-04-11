package it.domina.lib.store;

public enum StoreType {

	ORACLE("ORACLE"),
	SQLSERVER("SQLSERVER"),
	ODBC("ODBC"),
	AS400("AS400"),
	MYSQL("MYSQL"),
	XML("XML");
	
	private String tipo;
	
	private StoreType(String code){
		this.tipo = code;
	}
	
	@Override
	public String toString() {
		return this.tipo;
	}
	
}
