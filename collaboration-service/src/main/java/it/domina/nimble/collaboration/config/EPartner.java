package it.domina.nimble.collaboration.config;

import java.util.List;
import java.util.Vector;

import it.domina.lib.store.Row;
import it.domina.lib.store.exception.RecordNotFound;
import it.domina.lib.utilities.AbstractStorable;
import it.domina.nimble.collaboration.ServiceConfig;
import it.domina.nimble.collaboration.auth.type.IdentityUserType;

public class EPartner extends AbstractStorable {
	
	public static final String ID = "ID";
	public static final String USERID = "USERID";
	public static final String USERNAME = "USERNAME";
	public static final String FIRSTNAME = "FIRSTNAME";
	public static final String LASTNAME = "LASTNAME";
	public static final String EMAIL = "EMAIL";
	public static final String COMPANYID = "COMPANYID";
	
	private EPartner(){
		super("partners", "prt_id", ServiceConfig.getInstance().getStore());
	}

	protected EPartner(Row data){
		this();
		super.setValues(data);
	}

	public EPartner(Long id) throws RecordNotFound{
		this();
		super.load(id);
	}
	
	public EPartner(IdentityUserType user) {
		this();
		super.data.getField("prt_UserID").setValue(user.getUserID());
		super.data.getField("prt_username").setValue(user.getUsername());
		super.data.getField("prt_firstname").setValue(user.getFirstname());
		super.data.getField("prt_lastname").setValue(user.getLastname());
		super.data.getField("prt_email").setValue(user.getEmail());
		super.data.getField("prt_companyID").setValue(user.getCompanyID());
	}

	public EPartner(String userID, String username, String firstname, String lastname, String email, String companyID) {
		this();
		super.data.getField("prt_UserID").setValue(userID);
		super.data.getField("prt_username").setValue(username);
		super.data.getField("prt_firstname").setValue(firstname);
		super.data.getField("prt_lastname").setValue(lastname);
		super.data.getField("prt_email").setValue(email);
		super.data.getField("prt_companyID").setValue(companyID);
	}
	
	public String getUserId() {
		return super.data.getField("prt_UserID").getStringValue();
	}

	public String getUsername() {
		return super.data.getField("prt_username").getStringValue();
	}
	
	public String getFirstName() {
		return super.data.getField("prt_firstname").getStringValue();
	}

	public String getLastName() {
		return super.data.getField("prt_lastname").getStringValue();
	}

	public String getEmail() {
		return super.data.getField("prt_email").getStringValue();
	}

	public String getCompanyID() {
		return super.data.getField("prt_companyID").getStringValue();
	}

	public List<EProject> getSubscriptions()  throws Exception {
		List<EProject> prjout = new Vector<EProject>();
		VSubscriptions lst = new VSubscriptions(this);
		for (int i = 0; i < lst.getSize(); i++) {
			ESubscription sub = lst.getSubscription(i);
			EProject p =sub.getProject();
			if (p.getStatus() == ProjectStatus.ACTIVE) {
				prjout.add(p);
			}
		}
		return prjout;
	}
	
}
