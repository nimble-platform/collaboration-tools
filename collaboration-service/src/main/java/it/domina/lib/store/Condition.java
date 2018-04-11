package it.domina.lib.store;

public class Condition {

	private String key;
	private Object value;
	private ConditionType modo;
	
	public Condition(String key, ConditionType modo, Object value){
		this.key = key;
		this.value = value;
		this.modo = modo;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public ConditionType getModo() {
		return modo;
	}
	
}
