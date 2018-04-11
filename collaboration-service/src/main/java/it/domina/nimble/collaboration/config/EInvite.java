package it.domina.nimble.collaboration.config;

import it.domina.lib.store.Row;
import it.domina.lib.store.exception.RecordNotFound;
import it.domina.lib.utilities.AbstractStorable;
import it.domina.nimble.collaboration.ServiceConfig;

public class EInvite extends AbstractStorable {

	public static final String ID = "ID";
	public static final String PARTNER_FROM = "PARTNER_FROM";
	public static final String PROJECT = "PROJECT";
	public static final String USER = "USER";
	
	private EInvite(){
		super("invites", "inv_id", ServiceConfig.getInstance().getStore());
	}

	protected EInvite(Row data){
		this();
		super.setValues(data);
	}

	public EInvite(Long id) throws RecordNotFound{
		this();
		super.load(id);
	}

	public EInvite(EPartner prt, EProject prj, String user) {
		this();
		super.data.getField("inv_idprt_from").setValue(prt);
		super.data.getField("inv_idprj").setValue(prj);
		super.data.getField("inv_userid").setValue(user);
	}

	public EPartner getPartnerFrom() {
		try {
			return new EPartner(super.data.getField("inv_idprt_from").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public EProject getProject() {
		try {
			return new EProject(super.data.getField("inv_idprj").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public String getUser() {
		return super.data.getField("inv_userid").getStringValue();
	}


}
