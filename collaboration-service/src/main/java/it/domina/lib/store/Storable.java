package it.domina.lib.store;

import java.util.List;

public interface Storable {

	public void load(Long id) throws Exception;
	public Boolean save() throws Exception;
	public Boolean saveWithoutRefresh() throws Exception;
	public Boolean remove()throws Exception;
	
	public String getArchiveName();
	public Field getPK();
	public Row getValues();
	public Boolean setValues(Row info);
	public List<Storable> getChildren();
	
}
