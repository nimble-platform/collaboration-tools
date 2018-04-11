package it.domina.lib.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Row implements Cloneable {
	
	private HashMap<String, Field> 	fields = new HashMap<String, Field>();
	
	public List<Field> getFields() {
		return new ArrayList<Field>(this.fields.values());
	}

	public Field getField(String key){
		Field out = findField(key);
		if (out==null){
			out = new Field(key, FieldType.OBJECT, null,FieldAction.BOTH);
			addField(out);
		}
		return out;
	}

	public Field getField(String key, String value){
		Field out = findField(key);
		if (out==null){
			out = new Field(key, FieldType.STRING, value,FieldAction.BOTH);
			addField(out);
		}
		return out;
	}

	public Field getField(String key, BigDecimal value){
		Field out = findField(key);
		if (out==null){
			out = new Field(key, FieldType.NUMBER, value,FieldAction.BOTH);
			addField(out);
		}
		return out;
	}

	public Field getField(String key, Long value){
		Field out = findField(key);
		if (out==null){
			out = new Field(key, FieldType.LONG, value,FieldAction.BOTH);
			addField(out);
		}
		return out;
	}

	public Field getField(String key, Identificabile value){
		Field out = findField(key);
		if (out==null){
			if (value!=null){
				out = new Field(key, FieldType.LONG, value.getID(),FieldAction.BOTH);
			}
			else{
				out = new Field(key, FieldType.LONG, null,FieldAction.BOTH);
			}
			addField(out);
		}
		return out;
	}

	public Field getField(String key, Date value){
		Field out = findField(key);
		if (out==null){
			out = new Field(key, FieldType.DATE, value,FieldAction.BOTH);
			addField(out);
		}
		return out;
	}

	public Field getField(String key, Boolean value){
		Field out = findField(key);
		if (out==null){
			out = new Field(key, FieldType.BOOLEAN, value,FieldAction.BOTH);
			addField(out);
		}
		return out;
	}
	
	public Field getField(String key, FieldType type, Object value){
		Field out = findField(key);
		if (out==null){
			out = new Field(key, type, value,FieldAction.BOTH);
			addField(out);
		}
		return out;
	}
	
	public void removeField(String key){
		this.fields.remove(key);
	}
	
	public Integer size(){
		return this.fields.size();
	}
	
	public void addField(Field f){
		this.fields.put(f.getKey(), f);
	}
	
	@Override
	public String toString() {
		String out = "FIELDS: ";
		Iterator<Field> iFields = this.fields.values().iterator();
		while (iFields.hasNext()){
			out += iFields.next().toString()+";";
		}
		return out;
	}
	
	@Override
	public Row clone() {
		Row out = new Row();
		for (Field c: this.fields.values()){
			out.addField(c);
		}
		return out;
	}
	
	public Field hasField(String key){
		return this.fields.get(key);
	}
	
	private Field findField(String key){
		Field out = this.fields.get(key);
		return out;
	}
	
}
