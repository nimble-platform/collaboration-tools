package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.store.ConditionType;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VResourcesLog extends AbstractVistaStore {
	
	private EResource resource;
	
	public VResourcesLog(EResource res){
		super("resources_log", EResourceLog.ID, ServiceConfig.getInstance().getStore());
		addColumn(EResourceLog.ID, "rsl_id");
		addColumn(EResourceLog.RESOURSE, "rsl_idres");
		addColumn(EResourceLog.VERSION, "rsl_version");
		addColumn(EResourceLog.USER, "rsl_user");
		addColumn(EResourceLog.NOTES, "rsl_notes");
		addColumn(EResourceLog.TIMESTAMP, "rsl_date");
		this.resource = res;
		clearWhere();
	}

	@Override
	public void clearWhere() {
		super.clearWhere();
		if (this.resource!=null) {
			super.addWhere(EResourceLog.RESOURSE, ConditionType.EQUAL, this.resource.getID());
		}
	}
	
	public EResourceLog getResourceLog(Integer index){
		try {
			VistaItemStore item = super.getItem(index);
			return new EResourceLog(item.getRowData());
		} catch (ItemNotFound e) {
			return null;
		}
	}
	
}

