package it.domina.lib.store.mysql;


import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.store.Field;
import it.domina.lib.store.FieldType;

public class MYSQLField extends Field {

	private static final Logger logger = Logger.getLogger(MYSQLField.class);

	
	public MYSQLField(Integer i, ResultSetMetaData mtData, Object value){
		try {
			super.setKey(mtData.getColumnName(i));
			readType(mtData.getColumnType(i), mtData.getScale(i));
			readValue(value);
		} catch (SQLException e) {
			logger.log(Level.ERROR, e);
		}
	}

	public MYSQLField(Field f){
		super(f.getKey(),f.getType(), f.getValue(), f.getAction());
	}

	private void readType(Integer type, Integer scale){
		switch (type) {
			case Types.VARCHAR:
				super.setType(FieldType.STRING);
				break;
			case Types.LONGVARCHAR:
				super.setType(FieldType.STRING);
				break;
			case Types.INTEGER:
				super.setType(FieldType.LONG);
				break;
			case Types.BIGINT:
				super.setType(FieldType.LONG);
				break;
			case Types.DECIMAL:
				super.setType(FieldType.NUMBER);
				break;
			case Types.BOOLEAN:
				super.setType(FieldType.BOOLEAN);
				break;
			case Types.CHAR:
				super.setType(FieldType.STRING);
				break;
			case Types.DOUBLE:
				super.setType(FieldType.NUMBER);
				break;
			case Types.FLOAT:
				super.setType(FieldType.NUMBER);
				break;
			case Types.NUMERIC:
				if (scale>0){
					super.setType(FieldType.NUMBER);
				}
				else{
					super.setType(FieldType.LONG);
				}
				break;
			case Types.TIMESTAMP:
				super.setType(FieldType.DATE);
				break;
			case Types.DATE:
				super.setType(FieldType.DATE);
				break;
			default:
				super.setType(FieldType.OBJECT);
				break;
			}
	}
	
	private void readValue(Object val){
		if (val!=null){ 
			FieldType tipo = getType();
			switch (tipo){
				case STRING: 
					super.setValue(val.toString());
					return;
				case NUMBER: 
					super.setValue((BigDecimal)val);
					return;
				case LONG: 
					if (val instanceof BigDecimal){
						super.setValue(((BigDecimal)val).longValue());
					}
					else{
						super.setValue((Long)val);
					}
					return;
				case BOOLEAN: 
					super.setValue((Boolean)val);
					return;
				case DATE: 
					super.setValue((Date)val);
					return;
				default : 
					super.setValue(val);
					return;
			}
		}
	}


}
