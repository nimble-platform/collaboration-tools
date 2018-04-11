package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VInvites extends AbstractVistaStore {
	
	public VInvites(){
		super("invites", EInvite.ID, ServiceConfig.getInstance().getStore());
		addColumn(EInvite.ID, "inv_id");
		addColumn(EInvite.PARTNER_FROM, "inv_idprt_from");
		addColumn(EInvite.PROJECT, "inv_idprj");
		addColumn(EInvite.USER, "inv_userid");
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

