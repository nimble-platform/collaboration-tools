package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VProjects extends AbstractVistaStore {
	
	public VProjects(){
		super("projects", EProject.ID, ServiceConfig.getInstance().getStore());
		addColumn(EProject.ID, "prj_id");
		addColumn(EProject.NAME, "prj_name");
		addColumn(EProject.OWNER, "prj_idprt");
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

