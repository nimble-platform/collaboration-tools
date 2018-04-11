package it.domina.nimble.collaboration.services.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.collaboration.config.EProject;
import it.domina.nimble.utils.JsonType;

public class ProjectType extends JsonType {

    private static final Logger logger = Logger.getLogger(ProjectType.class);

	private String token;
    private String name;
	private String owner;
	private String ownerName;
    
	protected ProjectType() {}
	
	public ProjectType(EProject prj) {
    	this.name = prj.getName();
    	this.owner = prj.getOwner().getUserId();
    	this.ownerName = prj.getOwner().getUsername();
    	this.token = "";
    }
	
	public ProjectType(String token, String name) {
    	this.token = token;
    	this.name = name;
    }

    public String getToken() {
    	return this.token;
    }

	public String getName() {
    	return this.name;
    }

    public String getOwner() {
    	return this.owner;
    }

    public String getOwnerName() {
    	return this.ownerName;
    }

    public static ProjectType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ProjectType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}
}
