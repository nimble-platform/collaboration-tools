package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.store.ConditionType;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VSubscriptions extends AbstractVistaStore {
	
	private EPartner  partner;
	
	public VSubscriptions(){
		this(null);
	}

	public VSubscriptions(EPartner prt){
		super("subscriptions", ESubscription.ID, ServiceConfig.getInstance().getStore());
		addColumn(ESubscription.ID, "sbp_id");
		addColumn(ESubscription.PARTNER, "sbp_idprt");
		addColumn(ESubscription.PROJECT, "sbp_idprj");
		this.partner = prt;
		clearWhere();
	}

	@Override
	public void clearWhere() {
		super.clearWhere();
		if (this.partner!=null) {
			addWhere(ESubscription.PARTNER, ConditionType.EQUAL, this.partner.getID());
		}
	}
	
	public ESubscription getSubscription(Integer index){
		try {
			VistaItemStore item = super.getItem(index);
			return new ESubscription(item.getRowData());
		} catch (ItemNotFound e) {
			return null;
		}
	}
	
}

