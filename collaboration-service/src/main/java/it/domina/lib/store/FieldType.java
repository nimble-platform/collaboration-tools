package it.domina.lib.store;

public enum FieldType {

	STRING("STRING"),
	NUMBER("NUMBER"),
	LONG("LONG"),
	INTEGER("INTEGER"),
	BOOLEAN("BOOLEAN"),
	OBJECT("OBJECT"),
	LITTERAL("LITTERAL"),
	DATE("DATE"),
	TIMESTAMP("TIMESTAMP");
	
	private String val;
	
	private FieldType(String s){
		this.val = s;
	}
	
	public String toString() {
		return this.val;
	};
	
}
