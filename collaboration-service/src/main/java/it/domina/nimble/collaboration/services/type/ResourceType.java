package it.domina.nimble.collaboration.services.type;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

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
   	private String user;
   	private Long version;
   	private String resource;
   	private String image;
   	
   	private String season;
   	private String sector;
   	private String composition;
   	private String currency;
   	private String price;
    
   	private String notes;
   	private Date lastUpdate; 
   	
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

    public String getUser() {
    	return this.user;
    }

    public void setUser(String u) {
    	this.user = u;
    }

    public Long getVersion() {
    	return this.version;
    }

    public void setVersion(Long ver) {
    	this.version = ver;
    }

    public String getResource(){
    	return this.resource;
    }

    public void setResource(String res) {
    	this.resource = res;
    }

    /*
    public BytesType getResource(){
    	return this.resource;
    }

    public void setResource(BytesType res) {
    	this.resource = res;
    }
    */

    public String getImageData(){
    	return this.image;
    }

    public void setImageData(String res) {
    	this.image = res;
    }

    public String getSeason(){
    	return this.season;
    }

    public void setSeason(String s){
    	this.season = s;
    }

    public String getSector(){
    	return this.sector;
    }

    public void setSector(String s){
    	this.sector = s;
    }

    public String getComposition(){
    	return this.composition;
    }

    public void setComposition(String comp){
    	this.composition = comp;
    }

    public String getCurrency(){
    	return this.currency;
    }

    public void setCurrency(String cur){
    	this.currency = cur;
    }
    
    public String getPrice(){
    	return this.price;
    }

    public void setPrice(String p){
    	this.price = p;
    }

    public String getNotes(){
    	return this.notes;
    }

    public void setNotes(String notes){
    	this.notes = notes;
    }
    
    public void setLastUpdate(Date dt){
    	this.lastUpdate = dt;
    }

    public Date getLastUpdate(){
    	return this.lastUpdate;
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
