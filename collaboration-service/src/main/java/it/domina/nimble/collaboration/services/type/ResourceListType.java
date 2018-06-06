package it.domina.nimble.collaboration.services.type;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.utils.JsonType;

public class ResourceListType extends JsonType {

    private static final Logger logger = Logger.getLogger(ResourceListType.class);

    private String token;
   	private String projectName;
   	private String name;
   	private String type;
   	private Long version;
   	private String user;
   	private String notes;
   	private List<ResourceListType> children;
	
   	public ResourceListType() {}
   	
   	public ResourceListType(ResourceListType parent, String name, String type) 
   	{
   		this.token = "";
   		this.projectName = parent.getProjectName();
   		if (parent.getName()!=null) {
   			if (!parent.getName().equals("")) {
   				if (name==null) {
   					this.name = parent.getName();
   				}
   				else {
   					this.name = parent.getName() + ResourceType.RESOURCE_SEPARATOR + name;
   				}
   			}
   			else {
   	   			this.name = name;
   			}
   		}
   		else {
   			this.name = name;
   		}
   		this.type = type;
   		this.version = Long.valueOf(0);
   	}
   	
   	public ResourceListType(String token, String project){
   		this(token, project, "");
   	}

   	public ResourceListType(String token, String project, String name){
   		this.token = token;
   		this.projectName = project;
   		this.name = name;
   	}
   	
    public String getToken() {
    	return this.token;
    }
    
   	public String getProjectName() {
    	return this.projectName;
    }

    public String getName() {
    	return this.name;
    }

    public String getType() {
    	return this.type;
    }

    public Long getVersion() {
    	return this.version;
    }

    public void setVersion(Long ver) {
    	this.version = ver;
    }

    public String getUser() {
    	return this.user;
    }

    public void setUser(String user) {
    	this.user = user;
    }
    
    public String getNotes(){
    	return this.notes;
    }

    public void setNotes(String notes){
    	this.notes = notes;
    }
    
    public List<ResourceListType> getChildren() {
    	if (this.children==null) {
    		this.children = new Vector<ResourceListType>();
    	}
    	return this.children;
    }

	public static ResourceListType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ResourceListType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

}
