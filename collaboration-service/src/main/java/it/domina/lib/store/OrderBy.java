package it.domina.lib.store;

public class OrderBy {

	private String key;
	private OrderDirection direction;
	
	public OrderBy(String key, OrderDirection dir){
		this.key = key;
		this.direction = dir;
	}

	public String getKey() {
		return this.key;
	}

	public OrderDirection getDirection() {
		return this.direction;
	}
	
}
