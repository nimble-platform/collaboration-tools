package it.domina.nimble.collaboration.config;

import java.util.List;
import java.util.Vector;

import it.domina.lib.store.ConditionType;
import it.domina.lib.store.Row;
import it.domina.lib.store.exception.RecordNotFound;
import it.domina.lib.utilities.AbstractStorable;
import it.domina.nimble.collaboration.ServiceConfig;
import it.domina.nimble.collaboration.services.type.ResourceType;

public class EProject extends AbstractStorable {

	public static final String ID = "ID";
	public static final String NAME = "NAME";
	public static final String OWNER = "OWNER";
	public static final String STATUS = "STATUS";
	
	private EProject(){
		super("projects", "prj_id", ServiceConfig.getInstance().getStore());
	}

	protected EProject(Row data){
		this();
		super.setValues(data);
	}

	public EProject(Long id) throws RecordNotFound{
		this();
		super.load(id);
	}

	public EProject(String name, EPartner owner) {
		this();
		super.data.getField("prj_name").setValue(name);
		super.data.getField("prj_idprt").setValue(owner);
	}

	public String getName() {
		return super.data.getField("prj_name").getStringValue();
	}
	
	public void setName(String name){
		super.data.getField("prj_name").setValue(name);
	}

	public EPartner getOwner() {
		try {
			return new EPartner(super.data.getField("prj_idprt").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public ProjectStatus getStatus() {
		return ProjectStatus.valueOfSigla(super.data.getField("prj_status").getStringValue());
	}
	
	public void setStatus(ProjectStatus value){
		super.data.getField("prj_status").setValue(value.toString());
	}

	public String getKafkaTopic() {
		return "TPC_" + this.getID();
	}

	public String getFolderName() {
		return "TPC_" + this.getID();
	}

	public EResource saveResource(ResourceType res, String user) throws Exception {
		EResource result;
		VResources lstRes = new VResources(this);
		lstRes.addWhere(EResource.KEY, ConditionType.EQUAL, res.getKey());
		if (lstRes.getSize()>0) {
			result = lstRes.getResource(0);	
		}
		else {
			result = new EResource(this, res.getKey(), res.getType(), res.getName(),res.getExt());	
		}
		result.setUser(user);
		result.setVersion(res.getVersion());
		result.setNotes(res.getNotes());
		result.save();
		result.setRawData(res.getResource());
		result.setImageData(res.getImageData());
		return result;
	}

	public EResource getResourceDescription(String name) throws Exception {
		VResources lstRes = new VResources(this);
		if (name!=null) {
			lstRes.addWhere(EResource.KEY, ConditionType.EQUAL, name);
		}
		if (lstRes.getSize()>0) {
			return lstRes.getResource(0);
		}
		return null;
	}
	
	public List<EResource> getResourcesList(EResource res) throws Exception {
		List<EResource> result = new Vector<EResource>();
		VResources lstRes = new VResources(this);
		if (res!=null) {
			lstRes.addWhere(EResource.PARENT, ConditionType.EQUAL,res.getID());
		}
		else {
			lstRes.addWhere(EResource.PARENT, ConditionType.ISNULL, null);
		}
		for (int i = 0; i < lstRes.getSize(); i++) {
			result.add(lstRes.getResource(i));
		}
		return result;
	}

}
