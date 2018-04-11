package it.domina.nimble.collaboration.services.type;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class ResourceType extends JsonType {

	public static final String GROUP_TYPE = "GROUP_TYPE";
	public static final String RESOURCE_TYPE = "RESOURCE_TYPE";
	
	public static final String RESOURCE_SEPARATOR = "/";
	
	private static final Logger logger = Logger.getLogger(ResourceType.class);

   	private String projectName;
   	private String key;
   	private String name;
   	private String type;
   	private String ext;
   	private Integer version;
   	private BytesType resource;
   	
   	public ResourceType() {}
   	
    public ResourceType(String projectID, String key, String type) { 
    	this(projectID, key, type, null);
    }

    public ResourceType(String projectName, String key, String type, String ext) { 
    	this.projectName = projectName;
    	this.key = key;
    	this.type = type;
    	this.ext = ext;
		String[] lstBack = key.split(ResourceType.RESOURCE_SEPARATOR);
		this.name =  lstBack[lstBack.length-1];
    }

    public String getProjectName() {
    	return this.projectName;
    }

    public String getKey() {
    	return this.key;
    }

    public String getName() {
    	return this.name;
    }

    public String getType() {
    	return this.type;
    }

    public String getExt() {
    	return this.ext;
    }

    public Integer getVersion() {
    	return this.version;
    }

    public void setVersion(Integer ver) {
    	this.version = ver;
    }

    public BytesType getResource(){
    	return this.resource;
    }

    public void setResource(BytesType res) {
    	this.resource = res;
    }

    @JsonIgnore
    public InputStream getStream(){
    	return new ByteArrayInputStream(this.resource.getBytes());
    }
    
    public static ResourceType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ResourceType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}


}
