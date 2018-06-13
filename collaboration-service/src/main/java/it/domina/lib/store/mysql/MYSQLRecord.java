package it.domina.lib.store.mysql;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.store.Field;
import it.domina.lib.store.FieldAction;
import it.domina.lib.store.FieldType;
import it.domina.lib.store.Row;
import it.domina.lib.store.Storable;
import it.domina.lib.store.exception.RecordNotFound;

public class MYSQLRecord {

	private static final Logger logger = Logger.getLogger(MYSQLRecord.class);
	
	private Boolean 	  esiste;
	private Field      	  pk;
	private MYSQLArchive  archive;
	private Row 		  fields = new Row();
	
	protected MYSQLRecord(ResultSet rs, MYSQLArchive arc)throws RecordNotFound {
		this.esiste = true;
		this.archive = arc;
		mapFields(rs);
	}
	
	public MYSQLRecord (Field f, MYSQLArchive arc) throws RecordNotFound {
		this.esiste = false;
		this.pk = f;
		this.archive = arc;
		if (this.pk.getValue()!=null){
			load();
		}
		else{
			throw new RecordNotFound("");
		}
	}

	protected MYSQLRecord (Storable s, MYSQLArchive arc) {
		this.esiste = false;
		this.pk = s.getPK();
		this.archive = arc;
		if (this.pk.getValue()!=null){
			load();
		}
	}

	public Boolean save(Storable s) throws Exception{
		if (this.esiste){
			return updateRec(s);
		}
		else{
			return insertRec(s);
		}
	}
	
	
	public Row getInfo() {
		return this.fields;
	}
	
	private void load(){
		
		String sql = "SELECT * " +
					 "  FROM " + this.archive.getName() +
					 " WHERE " + this.pk.getKey() + " = ? ";
		
		List<Field> params = new ArrayList<Field>();
		params.add(this.pk);

		MYSQLConnection conn = null; 
		MYSQLConnectionPool pool = this.archive.getPool();
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = pool.newConnection();
			stm = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stm = MYSQLDB.parametri(stm, params);
			stm.executeQuery();
			rs = stm.getResultSet();
			mapFields(rs);
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}
		finally{
			try {
				if (rs!=null){
					rs.close();
				}
				if (stm!=null){
					stm.close();
				}
			} catch (SQLException e) {
			}
			conn.close();
		}
	}	
	
	private void mapFields(ResultSet rs) throws RecordNotFound {
		try{
			if (rs.isBeforeFirst()){
				if (rs.next()){
					ResultSetMetaData mtData = rs.getMetaData();
					this.esiste = true;
					Integer iColNum = mtData.getColumnCount()+1; 
					for (int i = 1; i < iColNum; i++) {
						this.fields.addField(new MYSQLField(i, mtData,  rs.getObject(i)));
					}
				}
			}
		}
		catch (Exception e) {
			throw new RecordNotFound("");
		}
	}
	
	private Boolean insertRec(Storable s) throws Exception{
		
		String sql = "";
		
		String fieldsList = "";
		String valuesList = "";
		Boolean first = true;
		ArrayList<Field> wh = new ArrayList<Field>();
		List<Field> flds = s.getValues().getFields();
		if (flds!=null){
			sql = "INSERT INTO " + this.archive.getName() + " ";
			for (Field fld : flds){
				if ((fld.getAction()==FieldAction.INSERT)|(fld.getAction()==FieldAction.BOTH)){
		        	MYSQLField f = new MYSQLField(fld);
		        	if (first) {
		            	fieldsList += f.getKey();
		            	if (f.getType()==FieldType.LITTERAL){
			            	valuesList += f.getStringValue();
		            	}
		            	else{
			            	valuesList += "?";
		            	}
		            	first = false;
		        	}
		        	else{
		            	fieldsList += ","+f.getKey();
		            	if (f.getType()==FieldType.LITTERAL){
			            	valuesList += ","+f.getStringValue();
		            	}
		            	else{
		            		valuesList += ",?";
		            	}
		        	}
	            	if (!(f.getType()==FieldType.LITTERAL)) {
	            		wh.add(f);
	            	}
				}
			}
			sql += "(" + fieldsList + ") VALUES (" + valuesList + ")";
			
			MYSQLConnection conn = null; 
			MYSQLConnectionPool pool = this.archive.getPool();
			PreparedStatement stm = null;
			ResultSet rs = null;
			try {
				conn = pool.newConnection();
				Field pk = s.getPK();
				stm = conn.prepareStatement(sql, new String[]{pk.getKey()});
				stm = MYSQLDB.parametri(stm, wh);
			 	stm.executeUpdate();
			 	rs = stm.getGeneratedKeys();
				if (rs.next()){
					ResultSetMetaData mtData = rs.getMetaData();
					Integer iColNum = mtData.getColumnCount()+1; 
					for (int i = 1; i < iColNum; i++) {
						String name = mtData.getColumnName(i);
						if (name.equals("GENERATED_KEY")){
							if (pk.getType()==FieldType.LONG){
								pk.setValue(rs.getLong(i));
							}
							else if (pk.getType()==FieldType.STRING){
								pk.setValue(rs.getString(i));
							}
						}
					}
				}
			}
			finally{
				try {
					if (rs!=null){
						rs.close();
					}
					if (stm!=null){
						stm.close();
					}
				} catch (SQLException e) {
				}
				conn.close();
			}
		}
		return true;

	}
	
	private Boolean updateRec(Storable s) throws Exception{
		String sql = "";
		Boolean first = true;
		List<Field> where = new ArrayList<Field>();

		List<Field> flds = s.getValues().getFields();
		if (flds!=null){
			Iterator<Field> iFields = flds.iterator();
			sql = "UPDATE " + this.archive.getName() + " ";
			while (iFields.hasNext()){
	        	MYSQLField f = new MYSQLField(iFields.next());
	        	if ((f.getAction()==FieldAction.UPDATE)|(f.getAction()==FieldAction.BOTH)){
		        	if (!this.fields.getField(f.getKey()).equals(f)){
			        	if (first) {
			            	if (f.getType()==FieldType.LITTERAL){
				            	sql += " SET " + f.getKey() + " = " + f.getStringValue();
			            	}
			            	else{
				            	sql += " SET " + f.getKey() + " = ? ";
			            	}
			            	first = false;
			        	}
			        	else{
			            	if (f.getType()==FieldType.LITTERAL){
				            	sql +=  f.getKey() + " = " + f.getStringValue();
			            	}
			            	else{
			            		sql +=  "," + f.getKey() + " = ? ";
			            	}
			        	}
			        	if (!(f.getType()==FieldType.LITTERAL)){
			        		where.add(f);
			        	}
		        	}
	        	}
			}
			if (first){
				return true;
			}
			Field pk = s.getPK();
			sql += " WHERE " + pk.getKey() + " = ? ";
			where.add(pk);
			
			MYSQLConnection conn = null; 
			MYSQLConnectionPool pool = this.archive.getPool();
			PreparedStatement stm = null;

			try {
				conn = pool.newConnection();
				stm = conn.prepareStatement(sql);
				stm = MYSQLDB.parametri(stm, where);
			 	stm.executeUpdate();
			} catch (Exception e) {
				logger.log(Level.ERROR, e);
			}
			finally{
				try {
					if (stm!=null){
						stm.close();
					}
				} catch (SQLException e) {
				}
				conn.close();
			}
		}
		return true;

	}
}
