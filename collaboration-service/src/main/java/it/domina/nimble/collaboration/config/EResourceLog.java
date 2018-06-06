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
	
	private EResourceLog(){
		super("resources_log", "rsl_id", ServiceConfig.getInstance().getStore());
	}

	protected EResourceLog(Row data){
		this();
		super.setValues(data);
	}

	public EResourceLog(Long id) throws RecordNotFound{
		this();
		super.load(id);
	}

	public EResourceLog(EResource res) {
		this();
		super.data.getField("rsl_idres").setValue(res);
		super.data.getField("rsl_version").setValue(res.getVersion());
		super.data.getField("rsl_user").setValue(res.getUser());
		super.data.getField("rsl_notes").setValue(res.getNotes());
		super.data.getField("rsl_date").setValue(res.getLastDate());
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

}
