package it.domina.lib.dbfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.dbfile.exceptions.FileAlreadyPresent;
import it.domina.lib.dbfile.exceptions.FileNotFound;

public class DBFilesFile implements DBFilesElement {

	private static final Logger logger = Logger.getLogger(DBFilesFile.class);

	private String 				path;
	private DBFilesFolder		parent;
	
	public DBFilesFile(String path, DBFilesFolder parent) throws FileAlreadyPresent, IOException {
		File fout = new File(parent.getFullPath()+File.separator+path);
		if (fout.exists()){
			throw new FileAlreadyPresent(path);
		}
		else{
			fout.createNewFile();
			fout.setReadable(true,false);
			fout.setWritable(true,false);
			this.path = fout.getName();
		}
		this.parent = parent;
	}
	
	public DBFilesFile(File fl, DBFilesFolder parent) throws FileNotFound {
		if (!fl.exists()){
			throw new FileNotFound(fl.getName());
		}
		this.path = fl.getName();
		this.parent = parent;
	}

	@Override
	public DBFilesType getType() {
		return DBFilesType.FILE;
	}

	@Override
	public String getKey() {
		return this.path;
	}
	
	@Override
	public DBFilesFolder getParent() {
		return this.parent;
	}
	
	@Override
	public String getFullPath() {
		return parent.getFullPath() + File.separator + this.path; 
	}

	public Boolean setData(InputStream data){
		try {
			File fout = new File(getFullPath());
			OutputStream outputStream = new FileOutputStream(fout);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = data.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.close();
			outputStream = null;
			System.gc();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
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
	
	public InputStream getData() {
		try {
			InputStream source = new FileInputStream(getFullPath());
			InputStream dest = IOUtils.toBufferedInputStream(source);
			source.close();
			source = null;
	        System.gc();
			return dest;
		} catch (Exception e) {
			return null;
		}
	}

}
