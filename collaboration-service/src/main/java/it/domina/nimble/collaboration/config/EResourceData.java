package it.domina.nimble.collaboration.config;

import it.domina.lib.store.Row;
import it.domina.lib.store.exception.RecordNotFound;
import it.domina.lib.utilities.AbstractStorable;
import it.domina.nimble.collaboration.ServiceConfig;

public class EResourceData extends AbstractStorable {

	public static final String ID = "ID";
	public static final String RESOURSE = "RESOURSE";
	public static final String RESOURSE_LOG = "RESOURSE_LOG";
	public static final String TYPE = "TYPE";
	public static final String ORDER = "ORDER";
	public static final String DATA = "DATA";
	
	private EResourceData(){
		super("resources_data", "rds_id", ServiceConfig.getInstance().getStore());
	}
	
	protected EResourceData(Row data){
		this();
		super.setValues(data);
	}

	public EResourceData(Long id) throws RecordNotFound{
		this();
		super.load(id);
	}

	public EResourceData(EResource res, Long order, String type) {
		this();
		super.data.getField("rsd_resid").setValue(res);
		super.data.getField("rsd_order").setValue(order);
		super.data.getField("rsd_type").setValue(type);
	}

	public EResourceData(EResourceLog resl, Long order, String type) {
		this();
		super.data.getField("rsd_rslid").setValue(resl);
		super.data.getField("rsd_order").setValue(order);
		super.data.getField("rsd_type").setValue(type);
	}

	public EResource getResource() {
		try {
			return new EResource(super.data.getField("rsd_resid").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public EResourceLog getResourceLog() {
		try {
			return new EResourceLog(super.data.getField("rsd_rslid").getLongValue());
		} catch (RecordNotFound e) {
			return null;
		}
	}

	public Long getOrder() {
		return super.data.getField("rsd_order").getLongValue();
	}

	public String getType() {
		return super.data.getField("rsd_type").getStringValue();
	}
	
	public String getData() {
		return super.data.getField("rsd_data").getStringValue();
	}

	public void setData(String value) {
		super.data.getField("rsd_data").setValue(value);
	}

}
