package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.store.ConditionType;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VResources extends AbstractVistaStore {
	
	private EProject project;
	
	public VResources(EProject prj){
		super("resources", EResource.ID, ServiceConfig.getInstance().getStore());
		addColumn(EResource.ID, "res_id");
		addColumn(EResource.PROJECT, "res_idprj");
		addColumn(EResource.KEY, "res_key");
		addColumn(EResource.TYPE, "res_type");
		addColumn(EResource.NAME, "res_name");
		addColumn(EResource.PARENT, "res_idres");
		addColumn(EResource.VERSION, "res_lastver");
		addColumn(EResource.USER, "res_user");
		addColumn(EResource.NOTES, "res_notes");
		addColumn(EResource.LASTUPDATE, "res_lastUpdate");
		this.project = prj;
		clearWhere();
	}

	@Override
	public void clearWhere() {
		super.clearWhere();
		if (this.project!=null) {
			super.addWhere(EResource.PROJECT, ConditionType.EQUAL, this.project.getID());
		}
	}
	
	public EResource getResource(Integer index){
		try {
			VistaItemStore item = super.getItem(index);
			return new EResource(item.getRowData());
		} catch (ItemNotFound e) {
			return null;
		}
	}
	
}

