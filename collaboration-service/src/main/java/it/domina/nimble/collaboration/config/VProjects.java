package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.store.ConditionType;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VProjects extends AbstractVistaStore {
	
	private Boolean bActives;
	
	public VProjects(Boolean active){
		super("projects", EProject.ID, ServiceConfig.getInstance().getStore());
		addColumn(EProject.ID, "prj_id");
		addColumn(EProject.NAME, "prj_name");
		addColumn(EProject.OWNER, "prj_idprt");
		addColumn(EProject.STATUS, "prj_status");
		this.bActives = active; 
		clearWhere();
	}

	@Override
	public void clearWhere() {
		super.clearWhere();
		if (this.bActives) {
			addWhere(EProject.STATUS, ConditionType.EQUAL, ProjectStatus.ACTIVE.toString());
		}
	}
	
	public EProject getProject(Integer index){
		try {
			VistaItemStore item = super.getItem(index);
			return new EProject(item.getRowData());
		} catch (ItemNotFound e) {
			return null;
		}
	}
	
}

