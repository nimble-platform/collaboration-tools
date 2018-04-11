package it.domina.lib.store;

public enum ConditionType {

	EQUAL("="),
	NOT_EQUAL("<>"),
	GREATER_EQUAL(">="),
	GREATER(">"),
	LESS_EQUAL("<="),
	LESS("<"),
	LIKE("LIKE"),
	START_WITH("LIKE"),
	END_WITH("LIKE"),
	ISNULL("IS NULL"),
	ISNOTNULL("IS NOT NULL"),
	CUSTOM(" ");
	
	private String val;

	public static ConditionType valueOfSigla(String s) {
		for (ConditionType c: ConditionType.values()){
			if (s.equals(c.val)){
				return c;
			}
		}
		return null;
	}

	
	private ConditionType(String s){
		this.val = s;
	}
	
	public String toString() {
		return this.val;
	};
	
	
}
