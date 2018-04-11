package it.domina.lib.store;

public interface Transaction {

	public void commit() throws Exception;
	public void rollback();
	
}
