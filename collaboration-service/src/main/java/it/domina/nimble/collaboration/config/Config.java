package it.domina.nimble.collaboration.config;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.store.ConditionType;
import it.domina.nimble.collaboration.core.Session;
import it.domina.nimble.collaboration.exceptions.ElementAlreadyPresenet;
import it.domina.nimble.collaboration.exceptions.ProjectNotFound;
import it.domina.nimble.collaboration.exceptions.SubscriptionRequired;
import it.domina.nimble.collaboration.services.type.InviteType;
import it.domina.nimble.collaboration.services.type.ProjectType;

public class Config {

	private static final Logger logger = Logger.getLogger(Config.class);

	public Config() {
		try {
			logger.log(Level.INFO, "Connector initialize");
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}
	}

	public EPartner newPartner(Session sess) throws Exception {
		VPartners lst = new VPartners();
		lst.addWhere(EPartner.USERID ,ConditionType.EQUAL, sess.getUser().getUserID());
		if (lst.getSize()==0) {
			EPartner prtOut = new EPartner(sess.getUser());
			prtOut.save();
			return prtOut;
		}
		else {
			throw new ElementAlreadyPresenet();	
		}
	}

	public EPartner getPartner(Session sess) {
		VPartners lst = new VPartners();
		lst.addWhere(EPartner.USERID ,ConditionType.EQUAL, sess.getUser().getUserID());
		if (lst.getSize()==0) {
			return null;
		}
		else {
			return lst.getPartner(0);
		}
	}
	
	public EProject newProject(ProjectType param, Session sess) throws Exception {
		EPartner prt = getPartner(sess);
		if (prt!=null) {
			VProjects lst = new VProjects();
			lst.addWhere(EProject.NAME, ConditionType.EQUAL, param.getName());
			lst.addWhere(EProject.OWNER, ConditionType.EQUAL, prt.getID());
			if (lst.getSize()==0) {
				EProject prjOut = new EProject(param.getName(), prt);
				prjOut.save();
				ESubscription newSub = new ESubscription(prt, prjOut);
				newSub.save();
				return prjOut;
			}
			else {
				throw new ElementAlreadyPresenet();	
			}
		}
		else {
			throw new SubscriptionRequired();	
		}
	}
	
	public String sendInvite(InviteType params, Session sess)  throws Exception {
		EPartner prt = getPartner(sess);
		if (prt!=null) {
			VProjects lstPrj = new VProjects();
			lstPrj.addWhere(EProject.NAME, ConditionType.EQUAL, params.getProjectName());
			if (lstPrj.getSize()>0) {
				EProject prj = lstPrj.getProject(0);
				EInvite inv = getInvite(prt, prj, params.getUserID());
				if (inv==null) {
					inv = new EInvite(prt, prj, params.getUserID());
					inv.save();
				}
				return inv.getID().toString();
			}
			else {
				throw new ProjectNotFound();
			}
		}
		else {
			throw new SubscriptionRequired();	
		}
	}
	
	public EInvite getInvite(EPartner prt, EProject prj, String userID) {
		VInvites lst = new VInvites();
		lst.addWhere(EInvite.PARTNER_FROM, ConditionType.EQUAL, prt.getID());
		lst.addWhere(EInvite.PROJECT, ConditionType.EQUAL, prj.getID());
		lst.addWhere(EInvite.USER, ConditionType.EQUAL, userID);
		if (lst.getSize()>0) {
			return lst.getInvite(0);
		}
		return null;
	}
	
	public EProject connectProject(Session sess, String inviteID) throws Exception {
		EPartner prt = getPartner(sess);
		if (prt!=null) {
			EInvite inv = new EInvite(Long.valueOf(inviteID));
			EProject prj = inv.getProject();
			ESubscription newSub = new ESubscription(prt, prj);
			newSub.save();
			return prj;
		}
		else {
			throw new SubscriptionRequired();	
		}
	}
	
	
	public List<EProject> getSubscriptions(Session sess)  throws Exception {
		EPartner prt = getPartner(sess);
		if (prt!=null) {
			return prt.getSubscriptions();
		}
		else {
			throw new SubscriptionRequired();	
		}
	}

	
    /*
    private Properties loadConfiguration( String fileName) {
        InputStream propsStream;
        try {
        	Properties applicationProperties = new Properties();
            propsStream = new FileInputStream(fileName);
            applicationProperties.load(propsStream);
            propsStream.close();
            return applicationProperties;
        } catch (IOException e) {
            logger.log(Level.ERROR, e);
        }
        return null;
    }

	private void loadProjects() {
		this.projects = new HashMap<String, Project>();
		List<Project> projectsList = this.storage.getProjects();
		for (Project prj: projectsList) {
			this.projects.put(prj.getProjectID(), prj);
		}
	}
	
	private void loadPartners() {
		this.partners = new HashMap<String, EPartner>();
		this.projectsSubscriptions = new HashMap<String, List<Project>>();
		List<EPartner> partnersList = this.storage.getPartners();
		for (EPartner prt: partnersList) {
			this.partners.put(prt.getUserId(), prt);
			loadProjectsSubscriptions(prt);
		}
	}
	
	private void loadProjectsSubscriptions(EPartner prt) {
		List<Project> lstProjects = new Vector<Project>();
		this.projectsSubscriptions.put(prt.getUserId(), lstProjects);
		List<String> lst = this.storage.getPartnerProjects(prt);
		for (String item: lst) {
			lstProjects.add(this.projects.get(item));
		}
	}
	*/

}
