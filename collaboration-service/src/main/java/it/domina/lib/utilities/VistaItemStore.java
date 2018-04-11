package it.domina.lib.utilities;

import it.domina.lib.store.Field;
import it.domina.lib.store.Row;

public class VistaItemStore implements VistaItem {

	private Vista		padre;
	private Row 			data;
	private String 			idKey;
	
	public VistaItemStore(Row d, String idKey, Vista padre){
		this.data = d;
		this.idKey = idKey;
		this.padre = padre;
	}

	@Override
	public Long getID() {
		if (this.idKey!=null){
			if (checkAlias(this.idKey)){
				return this.data.getField(this.idKey).getLongValue();
			}
			else{
				return this.data.getField(this.padre.getKey(this.idKey)).getLongValue();
			}
		}
		else{
			return null;
		}
	}

	@Override
	public int size() {
		return this.data.size();
	}

	@Override
	public String getItem(String key) {
		try{
			if (checkAlias(key)){
				return this.data.getField(key).getValue().toString();
			}
			else{
				return this.data.getField(this.padre.getKey(key)).getValue().toString();
			}
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public Row getRowData(){
		return this.data;
	}

	public Row filterCopyData(String prefix){
		Row out = this.data.clone();
		for (Field campo: out.getFields()){
			if (!campo.getKey().startsWith(prefix)){
				out.removeField(campo.getKey());
			}
		}
		return out;
	}
	
	private Boolean checkAlias(String key){
		if (this.padre.getKey(key).indexOf(" AS ")>0){
			return true;
		}else{
			return false;
		}
	}
	
}
