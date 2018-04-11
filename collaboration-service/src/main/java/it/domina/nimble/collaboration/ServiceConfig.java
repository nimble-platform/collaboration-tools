package it.domina.nimble.collaboration;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.dbfile.DBFiles;
import it.domina.lib.store.Store;
import it.domina.lib.store.mysql.MYSQLDB;
import it.domina.nimble.collaboration.auth.Authentication;
import it.domina.nimble.collaboration.config.Config;
import it.domina.nimble.collaboration.config.EPartner;
import it.domina.nimble.collaboration.config.EProject;
import it.domina.nimble.collaboration.core.Connector;
import it.domina.nimble.collaboration.core.Session;
import it.domina.nimble.collaboration.exceptions.SubscriptionRequired;
import it.domina.nimble.collaboration.kafka.KafkaCfg;
import it.domina.nimble.collaboration.services.type.CollabMessageType;
import it.domina.nimble.collaboration.services.type.CollaborateType;
import it.domina.nimble.collaboration.services.type.ReadMessageType;
import it.domina.nimble.collaboration.services.type.ReadResourceType;
import it.domina.nimble.collaboration.services.type.ResourceListType;
import it.domina.nimble.collaboration.services.type.ResourceType;
import it.domina.nimble.collaboration.services.type.SaveResourceType;
import it.domina.nimble.collaboration.services.type.SendMessageType;

public class ServiceConfig {

	private static final Logger logger = Logger.getLogger(ServiceConfig.class);
	
	private static ServiceConfig instance;
	
	public static ServiceConfig getInstance() {
		return instance;
	}

	public static ServiceConfig create(String basedir) {
		if (instance==null) {
			instance = new ServiceConfig(basedir);
		}
		return instance;
	}

	private KafkaCfg    					kafkcaConfiguration;
	private Authentication 					authentication;
	private Store 							dbconn;
	private DBFiles							repository;
	private Config 							config;
	private HashMap<String, Connector> 		activeCollaborations;
	
	
	private ServiceConfig(String basedir) {
		String classesDir = basedir + File.separator + "WEB-INF" + File.separator +"classes";
		this.kafkcaConfiguration = new KafkaCfg(classesDir);
		this.authentication = new Authentication(classesDir);
		this.dbconn = new MYSQLDB(classesDir);
		this.repository = new DBFiles(classesDir);
		this.config = new Config();
		this.activeCollaborations = new HashMap<String, Connector>();
		logger.log(Level.INFO, "Configuration initialized");
	}
	
	public DBFiles getRepository() {
		return this.repository;
	}
	
	public KafkaCfg getKafkaConfig() {
		return this.kafkcaConfiguration;
	}
	
	public Authentication getAuth() {
		return this.authentication;
	}

	public Store getStore() {
		return this.dbconn;
	}
	
	public Config getConfig() {
		return this.config;
	}

	public void startCollaboration(CollaborateType param, Session sess) throws Exception {
		List<EProject> lstPrj = this.config.getSubscriptions(sess);
		for (EProject prj : lstPrj) {
			if (prj.getName().equals(param.getProjectName())) {
				Connector conn = this.activeCollaborations.get(prj.getName());
				if (conn==null) {
					conn = new Connector(prj);
					this.activeCollaborations.put(prj.getName(), conn);
				}
				EPartner prt = this.config.getPartner(sess);
				conn.addPartner(prt);
			}
		}
	}
	
	public boolean sendMessage(SendMessageType params, Session sess) throws Exception {
		EPartner prt = this.config.getPartner(sess);
		for (CollabMessageType msg : params.getMessages()) {
			Connector conn = this.activeCollaborations.get(msg.getProjectName());
			if (conn!=null) {
				return conn.sendMessage(msg, prt);
			}
		}
		return false;
	}
	
	public void readMessages(ReadMessageType params, Session sess) throws Exception {
		EPartner prt = this.config.getPartner(sess);
		Connector conn = this.activeCollaborations.get(params.getProjectName());
		if (conn!=null) {
			conn.readMessage(params, prt);
		}
	}

	public boolean saveResource(SaveResourceType params, Session sess) throws Exception {
		EPartner prt = this.config.getPartner(sess);
		for (ResourceType res : params.getResources()) {
			Connector conn = this.activeCollaborations.get(res.getProjectName());
			if (conn!=null) {
				return conn.saveResource(res, prt);
			}
		}
		return false;
	}

	public ResourceListType readResourceList(ResourceListType params, Session sess) throws Exception {
		EPartner prt = this.config.getPartner(sess);
		Connector conn = this.activeCollaborations.get(params.getProjectName());
		if (conn!=null) {
			return conn.readResourceList(prt, params);
		}
		else{
			throw new SubscriptionRequired();
		}
	}

	
	public ResourceType readResource(ReadResourceType params, Session sess) throws Exception {
		EPartner prt = this.config.getPartner(sess);
		Connector conn = this.activeCollaborations.get(params.getProjectName());
		if (conn!=null) {
			return conn.readResource(params, prt);
		}
		else {
			throw new SubscriptionRequired();
		}
	}
}
