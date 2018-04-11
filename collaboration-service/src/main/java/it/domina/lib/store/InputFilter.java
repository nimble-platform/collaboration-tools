package it.domina.lib.store;

import java.util.List;
import java.util.Vector;

public class InputFilter implements Filter {

	private String	fromCondition;
	private List<String> columns;// = new HashMap<String, String>();

	private List<Condition> condizioni;
	private List<GroupBy> groupBy;
	private List<Condition> having;
	private List<OrderBy> direction;
	
	public InputFilter(String fromValue){
		this.fromCondition = fromValue;
		this.columns  = new Vector<String>();
		this.condizioni = new Vector<Condition>();
		this.groupBy = new Vector<GroupBy>();
		this.having = new Vector<Condition>();
		this.direction = new Vector<OrderBy>();
	}
	
	public InputFilter(Filter f){
		this.columns = f.getColumnsName();
		this.fromCondition = f.getFromCondition();
		this.condizioni = f.getWhere();
		if (this.columns==null){
			this.columns  = new Vector<String>();
		}
		if (this.condizioni==null){
			this.condizioni = new Vector<Condition>();
		}
		this.groupBy = f.getGroupBy();
		if (this.groupBy==null){
			this.groupBy = new Vector<GroupBy>();
		}
		this.having = f.getHaving();
		if (this.having==null){
			this.having = new Vector<Condition>();
		}
		this.direction = f.getOrderBy();
		if (this.direction==null){
			this.direction = new Vector<OrderBy>();
		}
	}

	public void clearColumnNames(){
		this.columns.clear();
	}

	public void addColumnNames(String name){
		this.columns.add(name);
	}
	
	public void clearWhere(){
		this.condizioni.clear();
	}
	
	public void addWhere(String key, ConditionType c, Object value){
		this.condizioni.add(new Condition(key, c, value));
	}

	public void clearGroupBy(){
		this.groupBy.clear();
	}
	
	public void addGroupBy(String key){
		this.groupBy.add(new GroupBy(key));
	}

	public void clearHaving(){
		this.having.clear();
	}
	
	public void addHaving(String key, ConditionType c, Object value){
		this.having.add(new Condition(key, c, value));
	}
	
	public void clearOrderBy(){
		this.direction.clear();
	}
	
	public void addOrderBy(String key, OrderDirection d){
		this.direction.add(new OrderBy(key, d));
	}

	@Override
	public List<String> getColumnsName() {
		return this.columns;
	}
	
	@Override
	public String getFromCondition() {
		return this.fromCondition;
	}

	@Override
	public List<Condition> getWhere() {
		return this.condizioni;
	}

	@Override
	public List<OrderBy> getOrderBy() {
		return this.direction;
	}

	@Override
	public List<GroupBy> getGroupBy() {
		return this.groupBy;
	}

	@Override
	public List<Condition> getHaving() {
		return this.having;
	}


}
