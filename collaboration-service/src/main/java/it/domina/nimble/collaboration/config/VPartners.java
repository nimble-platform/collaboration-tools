package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VPartners extends AbstractVistaStore {
	
	public VPartners(){
		super("partners", EPartner.ID, ServiceConfig.getInstance().getStore());
		addColumn(EPartner.ID, "prt_id");
		addColumn(EPartner.USERID, "prt_UserID");
		addColumn(EPartner.USERNAME, "prt_username");
		addColumn(EPartner.FIRSTNAME, "prt_firstname");
		addColumn(EPartner.LASTNAME, "prt_lastname");
		addColumn(EPartner.EMAIL, "prt_email");
		addColumn(EPartner.COMPANYID, "prt_companyID");
	}

	public EPartner getPartner(Integer index){
		try {
			VistaItemStore item = super.getItem(index);
			return new EPartner(item.getRowData());
		} catch (ItemNotFound e) {
			return null;
		}
	}
	
}

