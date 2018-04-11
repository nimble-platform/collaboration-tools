package it.domina.lib.dbfile;


public interface DBFilesElement {

	public String getKey();
	public String getFullPath();
	public DBFilesType getType();
	public DBFilesFolder getParent();
	//public String getPath();
	//public void setPath(String path) throws PathNotFound;
	
}
