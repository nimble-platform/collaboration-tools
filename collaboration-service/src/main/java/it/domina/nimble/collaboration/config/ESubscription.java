package it.domina.nimble.collaboration.config;

import it.domina.lib.store.Row;
import it.domina.lib.store.exception.RecordNotFound;
import it.domina.lib.utilities.AbstractStorable;
import it.domina.nimble.collaboration.ServiceConfig;

public class ESubscription extends AbstractStorable {

	public static final String ID = "ID";
	public static final String PARTNER = "PARTNER";
	public static final String PROJECT = "PROJECT";
	
	private ESubscription(){
		super("subscriptions", "sbp_id", ServiceConfig.getInstance().getStore());
	}

	protected ESubscription(Row data){
		this();
		super.setValues(data);
	}

	public ESubscription(Long id) throws RecordNotFound{
		this();
		super.load(id);
	}

	public ESubscription(EPartner prt, EProject prj) {
		this();
		super.data.getField("sbp_idprt").setValue(prt);
		super.data.getField("sbp_idprj").setValue(prj);
	}

	public EPartner getPartner() {
		try {
			return new EPartner(super.data.getField("sbp_idprt").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public EProject getProject() {
		try {
			return new EProject(super.data.getField("sbp_idprj").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

}
