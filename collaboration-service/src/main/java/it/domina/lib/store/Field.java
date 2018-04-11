package it.domina.lib.store;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Field {

	private String 			key;
	private FieldType 		type;
	private FieldAction 	action;
	private Object 			value;
	
	protected Field(){}
	
	public Field(String key, String value, FieldAction a){
		this.key = key;
		this.action = a;
		this.type = FieldType.STRING;
		this.value = value;
	}

	public Field(String key, BigDecimal value, FieldAction a){
		this.key = key;
		this.action = a;
		this.type = FieldType.NUMBER;
		this.value = value;
	}

	public Field(String key, Long value, FieldAction a){
		this.key = key;
		this.action = a;
		this.type = FieldType.LONG;
		this.value = value;
	}

	public Field(String key, Identificabile value, FieldAction a){
		this.key = key;
		this.action = a;
		this.type = FieldType.LONG;
		if (value!=null){
			this.value = value.getID();
		}
		else{
			this.value = null;
		}
	}

	public Field(String key, Date value, FieldAction a){
		this.key = key;
		this.action = a;
		this.type = FieldType.DATE;
		this.value = value;
	}

	public Field(String key, Date value, FieldAction a, Boolean timestamp){
		this.key = key;
		this.action = a;
		if (timestamp){
			this.type = FieldType.DATE;
		}
		else{
			this.type = FieldType.TIMESTAMP;
		}
		this.value = value;
	}

	public Field(String key, Boolean value, FieldAction a){
		this.key = key;
		this.action = a;
		this.type = FieldType.BOOLEAN;
		this.value = value;
	}

	public Field(String key, FieldType type, Object value, FieldAction a){
		this.key = key;
		this.action = a;
		this.type = type;
		this.value = value;
	}

	protected void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return this.key;
	}

	protected void setType(FieldType t){
		this.type = t;
	}

	public FieldType getType(){
		return this.type;
	}

	public FieldAction getAction(){
		return this.action;
	}

	public String getStringValue(){
		if (this.type==FieldType.STRING | this.type==FieldType.LITTERAL){
			return (String)this.value;
		}
		else{
			return null;
		}
	}

	public BigDecimal getNumberValue(){
		if (this.type==FieldType.NUMBER){
			if (this.value==null){
				return null;
			}
			else{
				return (BigDecimal)this.value;
			}
		}
		else{
			return null;
		}
	}

	public Long getLongValue(){
		if (this.type==FieldType.LONG){
			if (this.value==null){
				return null;
			}
			else{
				return (Long)this.value;
			}
		}
		else{
			return null;
		}
	}

	public Integer getIntegerValue(){
		if (this.type==FieldType.INTEGER){
			if (this.value==null){
				return null;
			}
			else{
				return (Integer)this.value;
			}
		}
		else{
			return null;
		}
	}

	public Date getDateValue(){
		if (this.type==FieldType.DATE){
			if (this.value==null){
				return null;
			}
			else{
				return (Date)this.value;
			}
		}
		else if (this.type==FieldType.TIMESTAMP){
			if (this.value==null){
				return null;
			}
			else{
				return (Date)this.value;
			}
		}
		else{
			return null;
		}
	}

	public Boolean getBooleanValue(){
		if (this.type==FieldType.BOOLEAN){
			if (this.value==null){
				return null;
			}
			else{
				return (Boolean)this.value;
			}
		}
		else{
			return null;
		}
	}

	public Object getValue(){
		return this.value;
	}
	
	public void setValue(String s){
		this.type = FieldType.STRING;
		this.value = s;
	}

	public void setValue(BigDecimal n){
		this.type = FieldType.NUMBER;
		this.value = n;
	}

	public void setValue(Long i){
		this.type = FieldType.LONG;
		this.value = i;
	}

	public void setValue(Integer i){
		this.type = FieldType.INTEGER;
		this.value = i;
	}

	public void setValue(Identificabile i){
		this.type = FieldType.LONG;
		if (i!=null){
			this.value = i.getID();
		}
		else{
			this.value = null;
		}
	}

	public void setValue(Date i){
		this.type = FieldType.DATE;
		this.value = i;
	}

	public void setValue(Date i, Boolean timestamp){
		if (timestamp){
			this.type = FieldType.TIMESTAMP;
		}
		else{
			this.type = FieldType.DATE;
		}
		this.value = i;
	}

	public void setValue(Boolean i){
		this.type = FieldType.BOOLEAN;
		this.value = i;
	}

	public void setValue(Object o){
		this.type = FieldType.OBJECT;
		this.value = o;
	}
	
	public void setValue(Object o, FieldType t){
		this.type = t;
		this.value = o;
	}

	@Override
	public String toString() {
		String out = this.key;
		if (this.value==null){
			out+="=[NULL]";
		}
		else{
			switch (this.type) {
				case BOOLEAN:
				if ((Boolean)this.value) {
					out+="=true"; 
				}
				else{
					out+="=false"; 
				}
				break;
			case LONG:
				out += "="+getLongValue().toString();
				break;
			case DATE:
				Date d = getDateValue();
				if (d!=null){
					SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
					out += "="+fm.format(d);
				}
				break;
			case TIMESTAMP:
				d = getDateValue();
				if (d!=null){
					SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					out += "="+fm.format(d);
				}
				break;
			case NUMBER:
				out += "="+getNumberValue().toString();
				break;
			case STRING:
				out += "="+getStringValue();
				break;
			default:
				out += "="+this.value.toString(); 
			} 
		}
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Field) {
			Field f = (Field) obj;
			if (f.key.equals(this.key)){
				if (f.getValue()==null){
					if (this.value==null){
						return true;
					}
					else{
						return false;
					}
				}
				else{
					if (this.value==null){
						return false;
					}
					else{
						switch (f.getType()) {
						case BOOLEAN:
							if (f.getBooleanValue().equals(this.getBooleanValue())){
								return true;
							}
							else{
								return false;
							}
						case LONG:
							if (f.getLongValue().equals(this.getLongValue())){
								return true;
							}
							else{
								return false;
							}
						case DATE:
							if (f.getDateValue().equals(this.getDateValue())){
								return true;
							}
							else{
								return false;
							}
						case TIMESTAMP:
							if (f.getDateValue().equals(this.getDateValue())){
								return true;
							}
							else{
								return false;
							}
						case NUMBER:
							if (f.getNumberValue().equals(this.getNumberValue())){
								return true;
							}
							else{
								return false;
							}
						case STRING:
							if (f.getStringValue().equals(this.getStringValue())){
								return true;
							}
							else{
								return false;
							}
						default:
							if (f.getValue().equals(this.getValue())){
								return true;
							}
							else{
								return false;
							}
						} 
					}
				}
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
}
