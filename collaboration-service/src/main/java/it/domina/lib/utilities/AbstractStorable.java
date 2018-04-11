package it.domina.lib.utilities;

import java.util.List;

import it.domina.lib.store.Field;
import it.domina.lib.store.FieldAction;
import it.domina.lib.store.FieldType;
import it.domina.lib.store.Identificabile;
import it.domina.lib.store.Row;
import it.domina.lib.store.Storable;
import it.domina.lib.store.Store;
import it.domina.lib.store.StoreType;
import it.domina.lib.store.exception.RecordNotFound;

public abstract class AbstractStorable implements Storable, Identificabile {

	private String						archiveName;
	private Store						db;
	private String 						idKey;
	
	protected Row						data;
	
	public AbstractStorable(String archive, String idKey, Store s){
		this.data = new Row();
		this.archiveName = archive;
		this.db = s;
		this.idKey = idKey;
		if (s.getType()==StoreType.ORACLE){
			this.data.addField(new Field(idKey, FieldType.LONG, null,FieldAction.INSERT));
		}
		else{
			this.data.addField(new Field(idKey, FieldType.LONG, null,FieldAction.NONE));
		}
	}

  	public Long getID() {
		return this.data.getField(this.idKey).getLongValue();
	}
	
	@Override
	public String getArchiveName() {
		return this.archiveName;
	}

	@Override
	public Field getPK() {
		return this.data.getField(this.idKey);
	}

	@Override
	public List<Storable> getChildren() {
		return null;
	}
	
	@Override
	public void load(Long id) throws RecordNotFound{
		this.data.getField(this.idKey).setValue(id);
		this.db.load(this);
	}

	@Override
	public Boolean save() throws Exception {
		if (this.db.save(this)){	
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean saveWithoutRefresh() throws Exception {
		if (this.db.save(this)){	
			return true;
		}
		return false;
	}

	@Override
	public Boolean remove() throws Exception {
		if (this.db.remove(this)){
			return true;
		}
		return false;
	}
	
	@Override
	public Row getValues() {
		return this.data;
	}

	@Override
	public Boolean setValues(Row info) { 
		for (Field f: info.getFields()){
			Field fto = this.data.getField(f.getKey()); 
			fto.setValue(f.getValue(), f.getType());
		}
		return true;
	}

}
