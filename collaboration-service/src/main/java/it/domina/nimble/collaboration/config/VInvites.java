package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.store.ConditionType;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VInvites extends AbstractVistaStore {
	

	private String username;
	
	public VInvites(){
		this(null);
	}
	
	public VInvites(String username){
		super("invites", EInvite.ID, ServiceConfig.getInstance().getStore());
		addColumn(EInvite.ID, "inv_id");
		addColumn(EInvite.PARTNER_FROM, "inv_idprt_from");
		addColumn(EInvite.PROJECT, "inv_idprj");
		addColumn(EInvite.USER, "inv_userid");
		this.username = username;
		clearWhere();
	}

	@Override
	public void clearWhere() {
		super.clearWhere();
		if (this.username != null) {
			addWhere(EInvite.USER,ConditionType.EQUAL, this.username);
		}
	}
	
	public EInvite getInvite(Integer index){
		try {
			VistaItemStore item = super.getItem(index);
			return new EInvite(item.getRowData());
		} catch (ItemNotFound e) {
			return null;
		}
	}
	
}

