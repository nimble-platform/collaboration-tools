package it.domina.nimble.collaboration.config;

import it.domina.lib.exceptions.ItemNotFound;
import it.domina.lib.store.ConditionType;
import it.domina.lib.store.OrderDirection;
import it.domina.lib.utilities.AbstractVistaStore;
import it.domina.lib.utilities.VistaItemStore;
import it.domina.nimble.collaboration.ServiceConfig;

public class VResourcesData extends AbstractVistaStore {
	
	private EResource resource;
	private EResourceLog resourcel;
	private String type;
	
	public VResourcesData(EResource res, String type){
		super("resources_data", EResourceData.ID, ServiceConfig.getInstance().getStore());
		addColumn(EResourceData.ID, "rds_id");
		addColumn(EResourceData.RESOURSE, "rsd_resid");
		addColumn(EResourceData.RESOURSE_LOG, "rsd_rslid");
		addColumn(EResourceData.TYPE, "rsd_type");
		addColumn(EResourceData.ORDER, "rsd_order");
		addColumn(EResourceData.DATA, "rsd_data");
		addOrderBy(EResourceData.ORDER, OrderDirection.ASCENDING);
		this.resource = res;
		this.type = type;
		clearWhere();
	}

	public VResourcesData(EResourceLog resl, String type){
		super("resources_data", EResourceData.ID, ServiceConfig.getInstance().getStore());
		addColumn(EResourceData.ID, "rds_id");
		addColumn(EResourceData.RESOURSE, "rsd_resid");
		addColumn(EResourceData.RESOURSE_LOG, "rsd_rslid");
		addColumn(EResourceData.TYPE, "rsd_type");
		addColumn(EResourceData.ORDER, "rsd_order");
		addColumn(EResourceData.DATA, "rsd_data");
		addOrderBy(EResourceData.ORDER, OrderDirection.ASCENDING);
		this.resourcel = resl;
		this.type = type;
		clearWhere();
	}

	@Override
	public void clearWhere() {
		super.clearWhere();
		if (this.resource!=null) {
			super.addWhere(EResourceData.RESOURSE, ConditionType.EQUAL, this.resource.getID());
			super.addWhere(EResourceData.RESOURSE_LOG, ConditionType.ISNULL,null);
			super.addWhere(EResourceData.TYPE, ConditionType.EQUAL,type);
		}
		else if (this.resourcel!=null) {
			super.addWhere(EResourceData.RESOURSE, ConditionType.ISNULL, null);
			super.addWhere(EResourceData.RESOURSE_LOG, ConditionType.EQUAL, this.resourcel.getID());
			super.addWhere(EResourceData.TYPE, ConditionType.EQUAL,type);
		}
	}
	
	public EResourceData getResourceData(Integer index){
		try {
			VistaItemStore item = super.getItem(index);
			return new EResourceData(item.getRowData());
		} catch (ItemNotFound e) {
			return null;
		}
	}
	
}

