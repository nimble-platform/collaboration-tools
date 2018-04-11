package it.domina.nimble.collaboration.core;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import it.domina.lib.store.Transaction;
import it.domina.nimble.collaboration.auth.type.IdentityUserType;

public class Session implements Serializable {
	
	private static final long serialVersionUID = -7166384931683716732L;

	//private String						key;
	private Date						data;
	private IdentityUserType 			user;
	private Map<String, Serializable> 	mapMem;
	//private Boolean						showMsg = true;
	//private Locale						prefLocale;

	private transient Boolean				isTransation = false;
	//private transient SimplyPublisher		eventManager;
	private transient Map<String, Object> 	mapTrans;
	private transient List<Transaction> 	transazioni;

	public Session(IdentityUserType u){
		//this.key = key;
		this.user = u;
		this.data = new Date(); 
		this.isTransation = false;
		//this.showMsg = true;
	}

	public String getKey() {
		return this.user.getAccessToken();
	}

	public Date getStartDate(){
		return this.data;
	}

	public IdentityUserType getUser(){
		return this.user;
	}
	
	public void setUser(IdentityUserType u){
		this.user = u;
	}
	/*
	public void setLocale(Locale loc){
		if (Sessions.getCurrent()==null){
			prefLocale = loc;
		}
		else{
			Sessions.getCurrent().setAttribute(Attributes.PREFERRED_LOCALE, loc);
		}
	}
	
	public Locale getLocale(){
		if (Sessions.getCurrent()==null){
			return prefLocale;
		}
		else{
			return (Locale)Sessions.getCurrent().getAttribute(Attributes.PREFERRED_LOCALE);
		}
	}
	*/
	
	public void set(String key, Object value){
		set(key,value,true);
	}

	public void set(String key, Object value, Boolean serialize){
		if (this.mapTrans == null){
			this.mapTrans = new HashMap<String, Object>();
		}
		if (this.mapMem == null){
			this.mapMem = new HashMap<String, Serializable>();
		}
		if (value==null){
			this.mapMem.remove(key);
			this.mapTrans.remove(key);
		}
		else{
			if (serialize){
				if (value instanceof Serializable){
					this.mapMem.put(key, (Serializable)value);
				}
				else{
					this.mapTrans.put(key, value);
				}
			}
			else{
				this.mapTrans.put(key, value);
			}
		}
	}
	
	public Object get(String key){
		if (this.mapTrans == null){
			this.mapTrans = new HashMap<String, Object>();
		}
		if (this.mapMem == null){
			this.mapMem = new HashMap<String, Serializable>();
		}
		Object out = this.mapMem.get(key);
		if (out==null){
			out = this.mapTrans.get(key);
		}
		return out;
	}

	public void clearMap(){
		if (this.mapMem!=null){
			this.mapMem.clear();
		}
		if (this.mapTrans!=null){
			this.mapTrans.clear();
		}
	}

	public void beginTrans() {
		this.isTransation = true;
	}
	
	public void commitTrans() throws Exception{
		if (isTransaction()){
			for (Transaction t: this.transazioni){
				t.commit();
			}
			this.transazioni.clear();
			this.isTransation = false;
		}
	}
	
	public void rollbackTrans() {
		if (isTransaction()){
			for (Transaction t: this.transazioni){
				t.rollback();
			}
			this.transazioni.clear();
			this.isTransation = false;
		}
	}
	
	
	public Boolean isTransaction(){
		if (this.isTransation==null){
			this.isTransation = false;
		}
		return this.isTransation;
	}

	public void registerTransaction(Transaction t){
		if (this.transazioni==null){
			this.transazioni = new Vector<Transaction>();
		}
		this.transazioni.add(t);
	}

	/*
	public Boolean isRunable(String cmd){
		EUser u = getUser();
		if (u!=null){
			return u.hasPermessoOf(cmd);
		}
		return true;
	}
	
	@Override
	public void addListener(MessageListener l) {
		if (this.eventManager == null){
			this.eventManager = new SimplyPublisher();
		}
		this.eventManager.addListener(l);
	}

	@Override
	public void removeListener(MessageListener l) {
		if (this.eventManager != null){
			this.eventManager.removeListener(l);
		}
	}

	@Override
	public void sendMessage(Message msg) {
		if (this.eventManager != null){ 
			this.eventManager.sendMessage(msg);
		}
	}

	@Override
	public void removeAllListener() {
		if (this.eventManager != null){
			this.eventManager.removeAllListener();
		}
	}
	*/
}
