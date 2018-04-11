package it.domina.lib.store;

import java.util.List;


public interface Filter {

	public List<String> getColumnsName();
	public String getFromCondition();
	public List<Condition> getWhere();
	public List<GroupBy> getGroupBy();
	public List<Condition> getHaving();
	public List<OrderBy> getOrderBy();
	
}
