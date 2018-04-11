package it.domina.lib.utilities;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.store.ConditionType;
import it.domina.lib.store.Filter;
import it.domina.lib.store.OrderDirection;

public interface Vista {
	
	public void clearColumns();
	public void addColumn(String name, String columnName);
	public void addColumn(String name, String columnName, Boolean visible);

	public String getKey(String name);
	
	public void addWhere(String key, ConditionType c, Object value);	
	public void clearWhere();

	public void addGroupBy(String key);	
	public void clearGroupBy();

	public void addHaving(String key, ConditionType c, Object value);	
	public void clearHaving();

	public void addOrderBy(String key, OrderDirection c);	
	public void clearOrderBy();

	public Filter getCurrentCondition();
	public Integer getSize();
	public VistaItem getItem(Integer index) throws ItemNotFound;
	public void requery();

}
