package it.domina.lib.store;


public interface DataSet {
	
	public int size();	
	
	public boolean next();
	public boolean previous();
	public boolean last();
	public boolean first();
	public boolean beforeFirst();
	public boolean moveTo(int pos);
	
	public Row getData();
	public Row getDataAt(int pos);

		
}
