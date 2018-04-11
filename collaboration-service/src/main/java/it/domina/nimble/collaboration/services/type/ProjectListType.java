package it.domina.nimble.collaboration.services.type;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.collaboration.config.EProject;
import it.domina.nimble.utils.JsonType;

public class ProjectListType extends JsonType {

    private static final Logger logger = Logger.getLogger(ProjectListType.class);

   	private List<ProjectType> projects;
	
   	protected ProjectListType() {}
   	
   	public ProjectListType(List<EProject> lst) {
    	this.projects = new Vector<ProjectType>();
    	for (EProject prj : lst) {
			this.projects.add(new ProjectType(prj));
		}
    }

    public List<ProjectType> getProjectList() {
    	if (this.projects== null) {
        	this.projects = new Vector<ProjectType>();
    	}
    	return this.projects;
    }

	public static ProjectListType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ProjectListType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

}
