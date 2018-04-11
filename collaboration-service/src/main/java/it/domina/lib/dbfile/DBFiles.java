package it.domina.lib.dbfile;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.dbfile.exceptions.FolderNotFound;

public class DBFiles {

	private static final Logger logger = Logger.getLogger(DBFiles.class);
	
	private DBFilesFolder	mainFolder;
	
	public DBFiles(String userdir){
		try {
			String propfile = userdir + File.separator + "dbfile.properties";
			Properties prop = loadConfiguration(propfile);
			this.mainFolder = new DBFilesFolder(prop.getProperty("path"), null);
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		} 
	}
	
	public List<DBFilesElement> getElementList() {
		return this.mainFolder.getElementList();
	}
	
	public DBFilesFolder getFolder(String name) throws FolderNotFound{
		return this.mainFolder.getFolder(name);
	}

	public DBFilesFolder getRootFolder() {
		return this.mainFolder;
	}

	public DBFilesFolder newFolder(String name){
		return this.mainFolder.newFolder(name);
	}

    private Properties loadConfiguration( String fileName) throws Exception {
        InputStream propsStream;
    	Properties applicationProperties = new Properties();
        propsStream = new FileInputStream(fileName);
        applicationProperties.load(propsStream);
        propsStream.close();
        return applicationProperties;
    }

}
