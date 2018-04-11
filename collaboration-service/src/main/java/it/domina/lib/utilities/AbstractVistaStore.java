package it.domina.lib.utilities;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.store.ConditionType;
import it.domina.lib.store.DataSet;
import it.domina.lib.store.Filter;
import it.domina.lib.store.InputFilter;
import it.domina.lib.store.OrderDirection;
import it.domina.lib.store.Store;

import java.util.HashMap;

public abstract class AbstractVistaStore implements Vista {

	private String						idKey;
	private InputFilter 				filtro;
	private DataSet     				data;
	private Boolean						requery;
	private Store						db;
	private HashMap<String, String> 	columns = new HashMap<String, String>();
	
	public AbstractVistaStore (String from, String idKey, Store s){
		this.idKey = idKey;
		this.filtro = new InputFilter(from);
		this.db = s;
		this.requery = true;
	}
	
	//TODO nuovo costrutture in cui imposti il pagesize

	public void setFilter(Filter f){
		this.filtro = new InputFilter(f);
	}
	
	@Override
	public void clearColumns(){
		this.columns.clear();
		this.filtro.clearColumnNames();
	}

	@Override
	public void addColumn(String name, String columnName){
		addColumn(name, columnName, true);
	}
	
	@Override
	public void addColumn(String name, String columnName, Boolean visible){
		if (visible){
			this.filtro.addColumnNames(columnName);
		}
		this.columns.put(name, columnName);
	}

	@Override
	public String getKey(String name) {
		String out = this.columns.get(name);
		if (out != null){
			return out;
		}
		else{
			return name;
		}
	}
	
	@Override
	public void addWhere(String key, ConditionType c, Object value) {
		this.filtro.addWhere(this.columns.get(key), c, value);
		this.requery = true;
	}

	@Override
	public void clearWhere() {
		this.filtro.clearWhere();
		this.requery = true;
	}

	@Override
	public void addGroupBy(String key) {
		this.filtro.addGroupBy(this.columns.get(key));
		this.requery = true;
	}
	
	@Override
	public void clearGroupBy() {
		this.filtro.clearGroupBy();
		this.requery = true;
	}
	
	@Override
	public void addHaving(String key, ConditionType c, Object value) {
		this.filtro.addHaving(this.columns.get(key),c,value);
		this.requery = true;
	}
	
	@Override
	public void clearHaving() {
		this.filtro.clearHaving();
		this.requery = true;
	}
	
	@Override
	public void addOrderBy(String key, OrderDirection d) {
		this.filtro.addOrderBy(this.columns.get(key), d);
		this.requery = true;
	}

	@Override
	public void clearOrderBy() {
		this.filtro.clearOrderBy();
		this.requery = true;
	}
	
	@Override
	public Filter getCurrentCondition() {
		return this.filtro;
	}
	
	@Override
	public Integer getSize() {
		if (this.requery){
			this.data = db.getLista(this.filtro);
			this.requery = false;
		}
		return this.data.size();
	}

	@Override
	public VistaItemStore getItem(Integer index) throws ItemNotFound {
		if (this.requery){
			this.data = db.getLista(this.filtro);
			this.requery = false;
		}
		if (this.data.getDataAt(index)!=null){
			return new VistaItemStore(this.data.getDataAt(index), this.idKey, this);
		}
		else{
			throw new ItemNotFound(index.toString());
		}
	}

	@Override
	public void requery(){
		this.requery = true;
	}
	
}
