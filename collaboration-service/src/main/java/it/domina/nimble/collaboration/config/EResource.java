package it.domina.nimble.collaboration.config;

import java.util.Date;
import java.util.List;
import java.util.Vector;

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
	public static final String NOTES = "NOTES";
	public static final String LASTUPDATE = "LASTUPDATE";
	
	private EResource _parentToSave = null;
	
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
	
	public void setVersion(Long ver){
		super.data.getField("res_lastver").setValue(ver);
		this._parentToSave = getParent();
		if (this._parentToSave!=null) {
			if (this._parentToSave.getVersion()<ver) {
				this._parentToSave.setVersion(ver);
			}
			else {
				this._parentToSave = null;
			}
		}
	}
	
	public String getUser() {
		return super.data.getField("res_user").getStringValue();
	}
	
	public void setUser(String user){
		super.data.getField("res_user").setValue(user);
	}
	
	public String getNotes() {
		return super.data.getField("res_notes").getStringValue();
	}
	
	public void setNotes(String note){
		super.data.getField("res_notes").setValue(note);
	}
	
	public Date getLastDate() {
		return super.data.getField("res_lastUpdate").getDateValue();
	}
	
	public List<EResourceLog> getResourcesHistory() throws Exception {
		List<EResourceLog> result = new Vector<EResourceLog>();
		VResourcesLog lstRes = new VResourcesLog(this);
		for (int i = 0; i < lstRes.getSize(); i++) {
			result.add(lstRes.getResourceLog(i));
		}
		return result;
	}
	
	public void removeVersionLog(Integer ver) {
		try {
			VResourcesLog lstRes = new VResourcesLog(this);
			lstRes.addWhere(EResourceLog.VERSION, ConditionType.EQUAL, ver);
			if (lstRes.getSize()>0) {
				EResourceLog log = lstRes.getResourceLog(0);
				log.remove();
			}
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public Boolean save() throws Exception {
		if (this._parentToSave!=null) {
			if (!this._parentToSave.save()) {
				return false;
			}
			this._parentToSave = null;
		}
		super.data.getField("res_lastUpdate").setValue(new Date());
		if (super.save()) {
			EResourceLog log = new EResourceLog(this);
			return log.save();
		}
		return false; 
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
