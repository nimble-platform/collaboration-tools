package it.domina.lib.dbfile;

import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DBFilesIndex", propOrder = {
    "name",
    "counter",
    "records"
})
@XmlRootElement(name = "DBFilesIndex")
public class DBFilesIndex {
	
	@XmlElement
	private String  				name;
	@SuppressWarnings("unused")
	@XmlElement
	private Integer  				counter;
    @XmlElement
	private List<DBFilesRecord>  	records;

    public DBFilesIndex(){
    }
    
    public DBFilesIndex(String name){
    	this.name = name;
    }
    
    public String getName() {
		return name;
	}
    
	public void setKey(String n) {
		this.name = n;
	}

    public Integer getNextSequence() {
		/*
    	if (counter==null){
			counter = getRecords().size()+1;
		}
    	return counter;
    	*/
    	return getRecords().size()+1;
	}
    
	/*
    public void setCounter(Integer c) {
		this.counter = c;
	}
	*/

	public List<DBFilesRecord> getRecords() {
		if (this.records==null){
			this.records = new Vector<DBFilesRecord>();
		}
		return records;
	}
	
	public void setRecords(List<DBFilesRecord> records) {
		this.records = records;
	}

}
