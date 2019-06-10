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
	public static final String EXT = "EXT";
	
	private EResource _parentToSave = null;
	private String _rawData;
	private String _imageData;
	
	private EResource(){
		super("resources", "res_id", ServiceConfig.getInstance().getStore());
	}

	protected EResource(Row data){
		this();
		super.setValues(data);
		loadRawData();
		loadImageData();
	}

	public EResource(Long id) throws RecordNotFound{
		this();
		super.load(id);
		loadRawData();
		loadImageData();
	}

	public EResource(EProject prj, String key, String type, String name, String ext) {
		this();
		super.data.getField("res_idprj").setValue(prj);
		super.data.getField("res_key").setValue(key);
		super.data.getField("res_type").setValue(type);
		super.data.getField("res_name").setValue(name);
		super.data.getField("res_ext").setValue(ext);
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

	public String getExt() {
		return super.data.getField("res_ext").getStringValue();
	}

	public void setExt(String e){
		super.data.getField("res_ext").setValue(e);
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
	
	public String getRawData() {
		return this._rawData;
	}
	
	public void setRawData(String rawData) {
		this._rawData = rawData;
	}

	public String getImageData() {
		return this._imageData;
	}

	public void setImageData(String imageData) {
		this._imageData = imageData;
	}
	
	/*
	private EResourceLog getCurrenResourcesLog() throws Exception {
		List<EResourceLog> result = new Vector<EResourceLog>();
		VResourcesLog lstRes = new VResourcesLog(this);
		lstRes.addWhere(EResourceLog.VERSION, ConditionType.EQUAL, this.getVersion());
		if (lstRes.getSize()>0) {
			return lstRes.getResourceLog(0);
		}
		else {
			return null;
		}
	}
	*/

	
	public List<EResourceLog> getResourcesHistory(Integer version) throws Exception {
		List<EResourceLog> result = new Vector<EResourceLog>();
		VResourcesLog lstRes = new VResourcesLog(this);
		if (version!=null) {
			lstRes.addWhere(EResourceLog.VERSION, ConditionType.EQUAL, version);
		}
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
			saveRawData();
			saveImageData();
			EResourceLog log = new EResourceLog(this);
			return log.save();
		}
		return false; 
	}
	
	private void loadRawData() {
		String resData = "";
		VResourcesData lstData = new VResourcesData(this,"R");
		for (Integer i= 0; i<lstData.getSize();i++) {
			EResourceData rd = lstData.getResourceData(i);
			resData+=rd.getData();
		}
		this._rawData = resData;
	}
	
	private void loadImageData() {
		String resData = "";
		VResourcesData lstData = new VResourcesData(this,"I");
		for (Integer i= 0; i<lstData.getSize();i++) {
			EResourceData rd = lstData.getResourceData(i);
			resData+=rd.getData();
		}
		this._imageData = resData;
	}
	
	private void saveRawData() {
		if (this._rawData!= null) {
			String rd = this._rawData; 
			Integer ord = 0;
			VResourcesData lstData = new VResourcesData(this,"R");
			for (Integer i= 0; i< lstData.getSize();i++) {
				EResourceData el = lstData.getResourceData(i);
				try {
					el.remove();
				} catch (Exception e) {
				}
			}
			while (rd.length()>0) {
				try {
					ord++;
					String tmpData = "";
					if (rd.length()>9999) {
						tmpData = rd.substring(0, 9999);
					}
					else {
						tmpData = rd;
					}
					EResourceData resData = new EResourceData(this,ord.longValue(),"R");
					resData.setData(tmpData);
					resData.save();
					if (rd.length()>9999) {
						rd = rd.substring(9999, rd.length());
					}
					else {
						rd = "";
					}
				} catch (Exception e) {
					return;
				}
			}
		}
	}
	
	private void saveImageData() {
		if (this._imageData != null) {
			String id = this._imageData; 
			Integer ord = 0;
			VResourcesData lstData = new VResourcesData(this,"I");
			for (Integer i= 0; i< lstData.getSize();i++) {
				EResourceData el = lstData.getResourceData(i);
				try {
					el.remove();
				} catch (Exception e) {
				}
			}
			while (id.length()>0) {
				try {
					ord++;
					String tmpData = "";
					if (id.length()>9999) {							
						tmpData = id.substring(0, 9999);
					}
					else {
						tmpData = id;
					}
					EResourceData resData = new EResourceData(this,ord.longValue(),"I");
					resData.setData(tmpData);
					resData.save();
					if (id.length()>9999) {
						id = id.substring(9999, id.length());
					}
					else {
						id = "";
					}
				} catch (Exception e) {
					return;
				}
			}
		}
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
