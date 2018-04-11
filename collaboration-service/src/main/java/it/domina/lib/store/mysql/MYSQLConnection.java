package it.domina.lib.store.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.store.Transaction;
import it.domina.nimble.collaboration.core.Session;

public class MYSQLConnection implements Transaction {

	private static final Logger logger = Logger.getLogger(MYSQLConnection.class);
	
	private String  sessionID;
	private Boolean bFree;
	private Connection conn;
	private Integer	pageSize;
	private Integer	timeout;
	
	private String  user;
	private String  pwd;
	private String  urlstr;

	public MYSQLConnection(String url, String usr, String psw) throws Exception {
		this.user = usr;
		this.urlstr = url;
		this.pwd = psw; 
		Class.forName ("com.mysql.jdbc.Driver");
		this.conn = DriverManager.getConnection(this.urlstr,this.user,this.pwd);
		this.conn.setAutoCommit(true);
		this.conn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
		this.bFree = true;
		this.pageSize = 101;
		this.timeout = 1;
	}

	public void setPageSize(Integer size){
		this.pageSize = size;
	}
	
	public Integer getPageSize(){
		return this.pageSize;
	}
	
	public void setTimeout(Integer to){
		this.timeout = to;
	}
	
	public Integer getTimeout(){
		return this.timeout;
	}

	public void keepAlive(){
		try {
			Statement stm = this.conn.createStatement();
			stm.execute("select 1+1");
		} catch (Exception e) {
			try {
				Class.forName ("com.mysql.jdbc.Driver");
				this.conn = DriverManager.getConnection(this.urlstr,this.user,this.pwd);
				this.conn.setAutoCommit(true);
				this.conn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
			} catch (Exception ex) {
				this.bFree = false;
				logger.log(Level.ERROR, ex);
			}
		}
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException{
		return this.conn.prepareStatement(sql);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException{
		return this.conn.prepareStatement(sql, columnNames);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException{
		return this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}
	
	public boolean isFree(){
		if (this.sessionID==null){
			return this.bFree;
		}
		else{
/*
			try {
				if (!this.conn.isValid(2)){
					Class.forName ("com.mysql.jdbc.Driver");
					this.conn  = DriverManager.getConnection(this.urlstr,this.user,this.pwd);
					this.bFree = true;
				}
			} catch (Exception e) {
				this.bFree = false;
				App.error(e);
			}
*/
			return false;
		}
	}

	public void setBusy(){
		this.bFree = false;
	}

	public void setBusyInTrans(Session sess){
		if (sess.isTransaction()){
			try {
				this.sessionID = sess.getKey();
				sess.registerTransaction(this);
				this.conn.setAutoCommit(false);
			} catch (SQLException e) {
				this.sessionID = null;
				logger.log(Level.ERROR, e);
			}
		}
	}

	public void close(){
		this.bFree = true;
	}

	public Boolean hasSession(Session sess){
		if (this.sessionID!=null){
			if (sess.getKey().equals(this.sessionID)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void commit() throws Exception{
		this.conn.commit();
		this.conn.setAutoCommit(true);
		this.sessionID = null;
	}

	@Override
	public void rollback(){
		try {
			this.conn.rollback();
			this.conn.setAutoCommit(true);
			this.sessionID = null;
		} catch (SQLException e) {
			logger.log(Level.ERROR, e);
		}
	}
	
	public void disconnect(){
		try {
			this.conn.close();
		} catch (SQLException e) {
		}
	}
	
}
