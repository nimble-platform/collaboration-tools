package it.domina.lib.store.mysql;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.store.Archive;
import it.domina.lib.store.DataSet;
import it.domina.lib.store.Field;
import it.domina.lib.store.FieldType;
import it.domina.lib.store.Filter;
import it.domina.lib.store.Storable;
import it.domina.lib.store.Store;
import it.domina.lib.store.StoreProcedure;
import it.domina.lib.store.StoreProcedureParams;
import it.domina.lib.store.StoreType;
import it.domina.lib.store.exception.ArchiveNotFound;
import it.domina.lib.store.exception.RecordNotFound;

public class MYSQLDB implements Store {

	private static final Logger logger = Logger.getLogger(MYSQLDB.class);

	
	public static FieldType readTipo(Object val){
		if (val!=null){
			if(val instanceof String) return FieldType.STRING;
			if(val instanceof Integer) return FieldType.INTEGER;
			if(val instanceof Float) return FieldType.NUMBER;
			if(val instanceof Long) return FieldType.LONG;
			if(val instanceof BigDecimal) return FieldType.NUMBER;
			if(val instanceof Boolean) return FieldType.BOOLEAN;
			if(val instanceof Date){
				String strData = val.toString();
				if (strData.substring(11,19).equals("00:00:00")){
					return FieldType.DATE;
				}
				else{
					return FieldType.TIMESTAMP;
				}
			}
		}
		return FieldType.STRING;
	}

	public static PreparedStatement parametri(PreparedStatement pst, List<Field> params)throws SQLException {
		if (params!=null){
			Integer i = 0;
			for (Field fld: params){
				if(fld.getValue()==null){
					pst.setNull(i+1,java.sql.Types.NULL);
				}
				else{
					if(fld.getType().equals(FieldType.STRING)) pst.setString(i+1, fld.getStringValue());
					if(fld.getType().equals(FieldType.DATE)) pst.setDate(i+1, new java.sql.Date(fld.getDateValue().getTime()));
					if(fld.getType().equals(FieldType.TIMESTAMP)) pst.setTimestamp(i+1, new java.sql.Timestamp(fld.getDateValue().getTime()));
					if(fld.getType().equals(FieldType.LONG)) pst.setLong(i+1, fld.getLongValue());
					if(fld.getType().equals(FieldType.NUMBER)) pst.setBigDecimal(i+1, fld.getNumberValue());
					if(fld.getType().equals(FieldType.INTEGER)) pst.setInt(i+1, fld.getIntegerValue());
					if(fld.getType().equals(FieldType.BOOLEAN)) pst.setBoolean(i+1, fld.getBooleanValue());
				}
				i++;
//				if(params[i] instanceof Integer) pst.setInt(i+1, (Integer)params[i]);
//				if(params[i] instanceof Float) pst.setFloat(i+1, (Float)params[i]);
			}
		}
		return pst;
	}

	
	private MYSQLConnectionPool pool;
	private HashMap<String, MYSQLArchive> archivi = new HashMap<String, MYSQLArchive>();

	public MYSQLDB(String conn, String user, String pwd) throws Exception{
		this.pool = new MYSQLConnectionPool(conn, user, pwd);
	}
	
	public MYSQLDB(String userdir) {
		try {
			JsonObject credentials = (new JsonParser()).parse(System.getenv("MYSQL_CREDENTIALS")).getAsJsonObject();
			String conn = credentials.get("conn").getAsString();
			String user = credentials.get("user").getAsString();
			String password = credentials.get("password").getAsString();

			this.pool = new MYSQLConnectionPool(conn, user, password);
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}
	}

	public void setPageSize(Integer size){
		this.pool.setPageSize(size);
	}
	
	@Override
	public List<Archive> getArchiveList() {
		List<Archive> out = new Vector<Archive>();

		String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
					 " WHERE TABLE_SCHEMA = (SELECT DATABASE() FROM DUAL) " + 
		 			 " ORDER BY TABLE_NAME ";
		
		MYSQLConnection conn = null; 
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = this.pool.newConnection();
			stm = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stm.executeQuery();
			rs = stm.getResultSet();
			rs.beforeFirst();
			while (rs.next()){
				out.add(new MYSQLArchive(rs.getString("TABLE_NAME"),this.pool));
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return out;
	}

	@Override
	public MYSQLArchive getArchivio(String name){
		MYSQLArchive out;
		try {
			out = this.archivi.get(name);
			if (out == null){ 
				out = new MYSQLArchive(name, this.pool);
				this.archivi.put(name, out);
			}
			return out;
		}
		catch (ArchiveNotFound e) {
			return null;
		}
		catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean load(Storable s) throws RecordNotFound{
		MYSQLArchive archivio = getArchivio(s.getArchiveName());
		if (archivio!=null){
			return archivio.load(s);
		} 
		else {
			throw new RecordNotFound(s.getArchiveName());
		}
	}

	@Override
	public Boolean save(Storable s) throws Exception{
		if (saveOnlyThis(s)){
			return saveChildren(s);
		}
		return false;
	}
	
	@Override
	public Boolean remove(Storable s) throws Exception {
		MYSQLArchive archivio = getArchivio(s.getArchiveName());
		if (archivio!=null){
			return archivio.remove(s);
		}
		else{
			return false;
		}
	}

	@Override
	public DataSet getLista(Filter f) {
		return new MYSQLDataSet(this.pool, f);
	}

	@Override
	public StoreType getType() {
		return StoreType.MYSQL;
	}
	
	@Override
	public StoreProcedure executeProcedure(StoreProcedureParams p) {
		return null;
	}
	
	private Boolean saveChildren(Storable s) throws Exception{
		List<Storable> chl = s.getChildren();
		if (chl!=null){
			for (Storable sc:chl){
				if (saveOnlyThis(sc)){
					if (!saveChildren(sc)){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private Boolean saveOnlyThis(Storable s) throws Exception{
		MYSQLArchive archivio = getArchivio(s.getArchiveName());
		if (archivio!=null){
			return archivio.save(s);
		}
		else{
			return false;
		}
	}

	@Override
	public void keepAlive() {
		this.pool.keepAlive();
	}

    private Properties loadConfiguration( String fileName) throws Exception {
        InputStream propsStream;
    	Properties applicationProperties = new Properties();
        propsStream = new FileInputStream(fileName);
        applicationProperties.load(propsStream);
        propsStream.close();
        return applicationProperties;
    }

}
