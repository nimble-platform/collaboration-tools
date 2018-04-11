package it.domina.lib.dbfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.dbfile.exceptions.FileAlreadyPresent;
import it.domina.lib.dbfile.exceptions.FileNotFound;
import it.domina.lib.dbfile.exceptions.FolderNotFound;

public class DBFilesFolder implements DBFilesElement {

	private static final Logger logger = Logger.getLogger(DBFilesFolder.class);

	private String 			path;
	private DBFilesFolder	parent;
	private String 			currentKey;

	public DBFilesFolder(String path, DBFilesFolder parent)  throws Exception{
		this.path = path;
		File fPath = null;
		if (parent != null){
			this.parent = parent;
			fPath = new File(parent.getFullPath()+File.separator+path);
		}
		else{
			this.parent = null;
			fPath = new File(path);
		}
		if (!fPath.exists()){
			fPath.mkdirs();
		}
	}

	public DBFilesFolder(File fl, DBFilesFolder parent)  throws Exception{
		if (!fl.exists()){
			throw new FileNotFound(fl.getName());
		}
		this.path = fl.getName();
		this.parent = parent;
	}

	@Override
	public DBFilesFolder getParent() {
		return this.parent;
	}
	
	@Override
	public DBFilesType getType() {
		return DBFilesType.FOLDER;
	}

	@Override
	public String getKey() {
		return this.path;
	}

	@Override
	public String getFullPath(){
		if (this.parent==null){
			return this.path;
		}
		else{
			return parent.getFullPath() + File.separator + this.path; 
		}
	}

	/**
	 * Ritorna il numero delle cartelle presenti
	 */
	public Integer getFolderCount() {
		return getFolderList().size();
	}

	/**
	 * Ritorna il numero dei files presenti
	 */
	public Integer getFilesCount() {
		return getFilesList().size();
	}

	public List<DBFilesElement> getElementList() {
		List<DBFilesElement> out = new Vector<DBFilesElement>();
		List<File> files = (List<File>)FileUtils.listFiles( new File(getFullPath()), TrueFileFilter.INSTANCE, null);
		try {
			for (File fl : files) {
				if (fl.isDirectory()){
					DBFilesFolder folder = new DBFilesFolder(fl, this); 
					out.add(folder);
				}
				else{
					DBFilesFile file = new DBFilesFile(fl,this);
					out.add(file);
				}
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}
		return out;
	}
	
	public List<DBFilesFolder> getFolderList() {
		List<DBFilesFolder> out = new Vector<DBFilesFolder>();
		File f = new File(getFullPath());
		List<File> files = Arrays.asList(f.listFiles());
		try {
			for (File fl : files) {
				if (fl.isDirectory()){
					DBFilesFolder folder = new DBFilesFolder(fl, this); 
					out.add(folder);
				}
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}
		return out;
	}

	public List<DBFilesFile> getFilesList() {
		List<DBFilesFile> out = new Vector<DBFilesFile>();
		try {
			File f = new File(getFullPath());
			File[] lstf = f.listFiles();
			if (lstf != null) {
				List<File> files = Arrays.asList(lstf);
				for (File fl : files) {
					if (fl.isFile()){
						DBFilesFile file = new DBFilesFile(fl, this);
						out.add(file);
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}
		return out;
	}

	public DBFilesFolder getFolder(String name) throws FolderNotFound{
		File fl = new File(getFullPath() + File.separator + name);
		if (fl.exists()){
			if (fl.isDirectory()){
				try {
					return new DBFilesFolder(fl, this); 
				} catch (Exception e) {
					logger.log(Level.ERROR, e);
					throw new FolderNotFound(name);
				}
			}
		}
		throw new FolderNotFound(name);
	}

	public DBFilesFolder newFolder(String name) {
		try {
			return new DBFilesFolder(name, this);
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}
		return null;
	}
	
	public String addFile(InputStream data) throws FileAlreadyPresent, IOException {
		DBFilesFile f = new DBFilesFile("FL"+getFilesCount().toString()+".xml", this);
		f.setData(data);
		return f.getKey();
	}
	
	public String addFile(InputStream data, String filename) throws FileAlreadyPresent, IOException {
		DBFilesFile f = new DBFilesFile(filename, this);
		f.setData(data);
		return f.getKey();
	}

	public DBFilesFile getFile(String name) throws FileNotFound{
		File fl = new File(getFullPath() + File.separator + name);
		if (fl.exists()){
			if (fl.isFile()){
				try {
					return new DBFilesFile(fl, this); 
				} catch (Exception e) {
					logger.log(Level.ERROR, e);
					throw new FileNotFound(name);
				}
			}
		}
		throw new FileNotFound(name);
	}
	
	public String getFilenameCaseInsensitive(String name) throws FileNotFound{
		File f = new File(getFullPath());
		List<File> files = Arrays.asList(f.listFiles());
		try {
			for (File fl : files) {
				if (fl.getName().equalsIgnoreCase(name)){
					return fl.getName(); 
				}
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
			throw new FileNotFound(name);
		}
		throw new FileNotFound(name);
	}

	public Boolean saveFile(String name, InputStream data) {
		try {
			File f = new File(getFullPath()+File.separator+name);
			if (f.exists() && !f.isDirectory()) {
				DBFilesFile fl = new DBFilesFile(f, this);
				fl.setData(data);
			}
			return true;
		} catch (FileNotFound e) {
			return false;
		}
	}
	
	public Boolean removeFile(String name) {
		File f = new File(getFullPath()+File.separator+name);
		if(f.exists() && !f.isDirectory()) {
			f.delete();
		}
		return true;
	}
	
	public Boolean delete(){
		File fout = new File(getFullPath());
		if (fout.exists()){
			try {
				return fout.delete();
			} catch (Exception e) {
				logger.log(Level.ERROR, e);
			}
		}
		return true;
	}
	
	/**
	 * Cancella tutto il contenuto della cartella
	 * @return false se ci sono stati errori
	 */
	public Boolean removeAllFiles(){
		try {
			FileUtils.cleanDirectory(new File(getFullPath()));
		} catch (IOException e) {
			logger.log(Level.ERROR, e);
			return false;
		} 
		return true;
	}

	@SuppressWarnings("null")
	public InputStream getNextFile(Boolean remove){
		InputStream out = null;
		Boolean find = false;
		Boolean findNext = false;
		DBFilesFile fl = null;
		File nextrec = null; 
		
		File fparent = new File(getFullPath());
		List<File> files = Arrays.asList(fparent.listFiles());
		try {
			for (File f : files) {
				if (!f.isDirectory()){
					if (findNext){
						this.currentKey = nextrec.getName();
						nextrec = f;
						find = true;
						break;
					}
					if (this.currentKey==null){
						this.currentKey = f.getName();
						nextrec = f;
						find = true;
						break;
					}
					else{
						if (this.currentKey.equals(f.getName())){
							findNext = true;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}
		if (find){
			try {
				fl = new DBFilesFile(nextrec, this);
				out = fl.getData();
				if (remove){
					fl.delete();
				}
			} catch (FileNotFound e) {
				logger.log(Level.ERROR, e);
			}
		}
		return out;
	}

}
