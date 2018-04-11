package it.domina.lib.store.mysql;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.domina.lib.store.Archive;
import it.domina.lib.store.Field;
import it.domina.lib.store.Row;
import it.domina.lib.store.Storable;
import it.domina.lib.store.exception.RecordNotFound;


public class MYSQLArchive implements Archive{

	private String 					name;
	private MYSQLConnectionPool 	poolConn;
	
	public MYSQLArchive(String name, MYSQLConnectionPool pool) throws Exception {
		this.name = name;
		this.poolConn = pool;
	}
	
	@Override
	public String getName(){
		return this.name;
	}

	@Override
	public Long getRecordCount(){
		Long out = new Long(0);
		String sql = "SELECT COUNT(*) FROM " + name;

		MYSQLConnection conn = null; 
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = this.poolConn.newConnection();
			stm = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stm.executeQuery();
			rs = stm.getResultSet();
			rs.beforeFirst();
			if (rs.next()){
				out = rs.getLong(0); 
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
	public Boolean save(Storable s) throws Exception{
		MYSQLRecord rec = new MYSQLRecord(s, this);
		if (rec.save(s)){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public Boolean load(Storable s) throws RecordNotFound {
		MYSQLRecord rec = new MYSQLRecord(s.getPK(), this);
		Row data = rec.getInfo();
		s.setValues(data);
		return true;
	}
	
	@Override
	public Boolean remove(Storable s) throws Exception {
		String sql = "DELETE " +
					 "  FROM " + this.name +
					 " WHERE " + s.getPK().getKey() + " = ? ";
			
		List<Field> params = new ArrayList<Field>();
		params.add(s.getPK());

		MYSQLConnection conn = null; 
		PreparedStatement stm = null;
		try {
			conn = this.poolConn.newConnection();
			stm = conn.prepareStatement(sql);
			stm = MYSQLDB.parametri(stm, params);
	 		stm.executeUpdate();
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
		return true;
	}

	public MYSQLConnectionPool getPool(){
		return this.poolConn;
	}

}
