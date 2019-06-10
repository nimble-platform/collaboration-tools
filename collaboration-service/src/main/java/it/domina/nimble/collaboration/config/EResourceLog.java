package it.domina.nimble.collaboration.config;

import java.util.Date;

import it.domina.lib.store.Row;
import it.domina.lib.store.exception.RecordNotFound;
import it.domina.lib.utilities.AbstractStorable;
import it.domina.nimble.collaboration.ServiceConfig;

public class EResourceLog extends AbstractStorable {

	public static final String ID = "ID";
	public static final String RESOURSE = "RESOURSE";
	public static final String VERSION = "VERSION";
	public static final String USER = "USER";
	public static final String NOTES = "NOTES";
	public static final String TIMESTAMP = "TIMESTAMP";
	
	private String strRawData;
	private String strImageData;
	
	private EResourceLog(){
		super("resources_log", "rsl_id", ServiceConfig.getInstance().getStore());
	}

	protected EResourceLog(Row data){
		this();
		super.setValues(data);
		loadImageData();
		loadRawData();
	}

	public EResourceLog(Long id) throws RecordNotFound{
		this();
		super.load(id);
		loadImageData();
		loadRawData();
	}

	public EResourceLog(EResource res) {
		this();
		super.data.getField("rsl_idres").setValue(res);
		super.data.getField("rsl_version").setValue(res.getVersion());
		super.data.getField("rsl_user").setValue(res.getUser());
		super.data.getField("rsl_notes").setValue(res.getNotes());
		super.data.getField("rsl_date").setValue(res.getLastDate());
		this.strRawData = res.getRawData();
		this.strImageData = res.getImageData();
	}

	public EResource getResource() {
		try {
			return new EResource(super.data.getField("rsl_idres").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public Long getVersion() {
		return super.data.getField("rsl_version").getLongValue();
	}

	public String getUser() {
		return super.data.getField("rsl_user").getStringValue();
	}
	
	public String getNotes() {
		return super.data.getField("rsl_notes").getStringValue();
	}

	public Date getTimestamp() {
		return super.data.getField("rsl_date").getDateValue();
	}

	@Override
	public Boolean save() throws Exception {
		super.save();
		saveRawData();
		saveImageData();
		return true;
	}
	
	public String getRawData() {
		return this.strRawData;
	}

	public String getImageData() {
		return this.strImageData;
	}

	private void saveRawData() {
		if (this.strRawData != null) {
			Integer ord = 0;
			while (this.strRawData.length()>0) {
				try {
					ord++;
					String tmpData = "";
					if (this.strRawData.length()>9999) {
						tmpData = this.strRawData.substring(0, 9999);
					}
					else {
						tmpData = this.strRawData;
					}
					EResourceData resData = new EResourceData(this,ord.longValue(),"R");
					resData.setData(tmpData);
					resData.save();
					if (this.strRawData.length()>9999) {
						this.strRawData = this.strRawData.substring(9999, this.strRawData.length());
					}
					else {
						this.strRawData = "";
					}
				} catch (Exception e) {
					return;
				}
			}
		}
	}

	private void saveImageData() {
		if (this.strImageData != null) {
			Integer ord = 0;
			while (this.strImageData.length()>0) {
				try {
					ord++;
					String tmpData = "";
					if (this.strImageData.length()>9999) {							
						tmpData = this.strImageData.substring(0, 9999);
					}
					else {
						tmpData = this.strImageData;
					}
					EResourceData resData = new EResourceData(this,ord.longValue(),"I");
					resData.setData(tmpData);
					resData.save();
					if (this.strImageData.length()>9999) {
						this.strImageData = this.strImageData.substring(9999, this.strImageData.length());
					}
					else {
						this.strImageData = "";
					}
				} catch (Exception e) {
					return;
				}
			}
		}
		
	}

	private void loadRawData() {
		String resData = "";
		VResourcesData lstData = new VResourcesData(this,"R");
		for (Integer i= 0; i<lstData.getSize();i++) {
			EResourceData rd = lstData.getResourceData(i);
			resData+=rd.getData();
		}
		this.strRawData = resData;
	}
	
	private void loadImageData() {
		String resData = "";
		VResourcesData lstData = new VResourcesData(this,"I");
		for (Integer i= 0; i<lstData.getSize();i++) {
			EResourceData rd = lstData.getResourceData(i);
			resData+=rd.getData();
		}
		this.strImageData = resData;
	}

	
}
