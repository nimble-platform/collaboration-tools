package it.domina.lib.store;

import it.domina.lib.store.exception.RecordNotFound;

import java.util.List;


public interface Store {

	public StoreType  getType();
	public Archive getArchivio(String name);
	public List<Archive> getArchiveList();
	
	public Boolean load(Storable s)throws RecordNotFound;
	public Boolean save(Storable s)throws Exception;
	public Boolean remove(Storable s) throws Exception;
	
	public DataSet getLista(Filter f);
	public StoreProcedure executeProcedure(StoreProcedureParams p);
	public void keepAlive();
	
}
