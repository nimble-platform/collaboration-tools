package it.domina.lib.dbfile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DBFilesRecord", propOrder = {
    "key",
    "type",
    "path"
})
public class DBFilesRecord {

    @XmlElement
	private String  				key;
    @XmlElement
	private DBFilesType  			type;
    @XmlElement
	private String  				path;
    
    public DBFilesRecord(){
    	
    }
    
    public DBFilesRecord(String name, DBFilesType tipo, String path){
    	this.key = name;
    	this.type = tipo;
    	this.path = path;
    }

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public DBFilesType getType() {
		return type;
	}
	public void setType(DBFilesType type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	
}
