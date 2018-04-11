package it.domina.nimble.collaboration.config;

import it.domina.lib.store.ConditionType;
import it.domina.lib.store.Row;
import it.domina.lib.store.exception.RecordNotFound;
import it.domina.lib.utilities.AbstractStorable;
import it.domina.nimble.collaboration.ServiceConfig;
import it.domina.nimble.collaboration.services.type.ResourceType;

public class EResource extends AbstractStorable {

	public static final String ID = "ID";
	public static final String PROJECT = "PROJECT";
	public static final String KEY = "KEY";
	public static final String PARENT = "PARENT";
	public static final String NAME = "NAME";
	public static final String TYPE = "TYPE";
	public static final String VERSION = "VERSION";
	public static final String USER = "USER";
	
	private EResource(){
		super("resources", "res_id", ServiceConfig.getInstance().getStore());
	}

	protected EResource(Row data){
		this();
		super.setValues(data);
	}

	public EResource(Long id) throws RecordNotFound{
		this();
		super.load(id);
	}

	public EResource(EProject prj, String key, String type, String name) {
		this();
		super.data.getField("res_idprj").setValue(prj);
		super.data.getField("res_key").setValue(key);
		super.data.getField("res_type").setValue(type);
		super.data.getField("res_name").setValue(name);
		EResource parent = getParentResourceByKey(key);
		super.data.getField("res_idres").setValue(parent);
	}

	public EProject getProject() {
		try {
			return new EProject(super.data.getField("res_idprj").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public EResource getParent() {
		try {
			return new EResource(super.data.getField("res_idres").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public String getKey() {
		return super.data.getField("res_key").getStringValue();
	}

	public String getName() {
		return super.data.getField("res_name").getStringValue();
	}
	
	public void setName(String name){
		super.data.getField("res_name").setValue(name);
	}

	public String getType() {
		return super.data.getField("res_type").getStringValue();
	}
	
	public void setType(String type){
		super.data.getField("res_type").setValue(type);
	}

	public Long getVersion() {
		return super.data.getField("res_lastver").getLongValue();
	}
	
	public void setVersion(Integer ver){
		super.data.getField("res_lastver").setValue(ver);
	}
	
	public String getUser() {
		return super.data.getField("res_user").getStringValue();
	}
	
	public void setUser(String user){
		super.data.getField("res_user").setValue(user);
	}
	
	private EResource getParentResourceByKey(String resourceKey) {
		String resParentName = null;
		String[] lstBack = resourceKey.split(ResourceType.RESOURCE_SEPARATOR);
		for (int i = 0; i< lstBack.length-1;i++) {
			if (resParentName == null) {
				resParentName = lstBack[i];
			}
			else {
				resParentName += ResourceType.RESOURCE_SEPARATOR + lstBack[i];
			}
		}
		if (resParentName!=null) {
			VResources lstRes = new VResources(getProject());
			lstRes.addWhere(EResource.NAME, ConditionType.EQUAL, resParentName);
			if (lstRes.getSize() > 0) {
				return lstRes.getResource(0);
			}
		}
		return null;
	}
	
}
