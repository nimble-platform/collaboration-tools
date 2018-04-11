package it.domina.lib.store;

import it.domina.lib.store.exception.RecordNotFound;


public interface Archive {
	
	public String getName();
	public Long getRecordCount();

	public Boolean load(Storable s) throws RecordNotFound;
	public Boolean remove(Storable s) throws Exception;
	public Boolean save(Storable s) throws Exception;
	
}
