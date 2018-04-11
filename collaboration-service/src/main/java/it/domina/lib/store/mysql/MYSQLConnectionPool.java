package it.domina.lib.store.mysql;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.nimble.collaboration.core.Session;

public class MYSQLConnectionPool {

	private static final Logger logger = Logger.getLogger(MYSQLConnectionPool.class);
	
	private String url;
	private String usr;
	private String psw;
	private Integer pageSize;
	
	private List<MYSQLConnection> conList;

	public MYSQLConnectionPool(String url, String usr, String psw){
		this.url=url;
		this.usr=usr;
		this.psw=psw;
		this.pageSize = 101;
		this.conList = new Vector<MYSQLConnection>();
	}
	
	public Integer getPageSize(){
		return this.pageSize;
	}
	
	public void setPageSize(Integer size){
		this.pageSize = size;
	}
	
	public MYSQLConnection newConnection() throws Exception {
		MYSQLConnection out = null;
		Boolean isTransaction = false; 
		Session sess = null;
		if (sess!=null){
			if (sess.isTransaction()){
				isTransaction = true;
			}
		}
		if (isTransaction){
			for (MYSQLConnection conn: this.conList){
				if (conn.hasSession(sess)){
					return conn;
				}
			}
			for (MYSQLConnection conn: this.conList){
				if (conn.isFree()){
					conn.setBusyInTrans(sess);
					return conn;
				}
			}
		}
		else{
			Iterator<MYSQLConnection> iter = this.conList.iterator();
			while (iter.hasNext()){
				out = iter.next();
				if (out.isFree()){
					out.setBusy();
					return out;
				}
			}
		}
		try {
			out = new MYSQLConnection(this.url, this.usr, this.psw);
			out.setPageSize(this.pageSize);
			this.conList.add(out);
			if (isTransaction){
				out.setBusyInTrans(sess);
			}
			else{
				out.setBusy();
			}
			return out;
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
			return null;
		}
	}

	public Iterator<MYSQLConnection> iterator(){
		return this.conList.iterator();
	}
	
	public void close(MYSQLConnection con) {
		con.close();
	}

	public void keepAlive(){
		Boolean first  = true;
		for (MYSQLConnection c: this.conList){
			if (c.isFree()){
				if (first){
					c.keepAlive();
				}
				else{
					if (c.getTimeout()==0){
						c.close();
						this.conList.remove(c);
					}
					else{
						c.setTimeout(c.getTimeout()-1);
						c.keepAlive();
					}
				}
			}
		}
	}
	
}
