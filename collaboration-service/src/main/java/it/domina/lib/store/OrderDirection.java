package it.domina.lib.store;

public enum OrderDirection {

	ASCENDING("ASC"),
	DESCENDING("DESC");
	
	public static OrderDirection valueOfSigla(String s) {
		for (OrderDirection o: OrderDirection.values()){
			if (s.equals(o.val)){
				return o;
			}
		}
		return null;
	}
	
	private String val;
	
	private OrderDirection(String s){
		this.val = s;
	}
	
	public String toString() {
		return this.val;
	};
	
}
