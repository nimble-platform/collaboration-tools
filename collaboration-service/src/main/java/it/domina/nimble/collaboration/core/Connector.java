package it.domina.nimble.collaboration.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.dbfile.DBFiles;
import it.domina.lib.dbfile.DBFilesFile;
import it.domina.lib.dbfile.DBFilesFolder;
import it.domina.lib.dbfile.exceptions.FolderNotFound;
import it.domina.nimble.collaboration.ServiceConfig;
import it.domina.nimble.collaboration.config.EPartner;
import it.domina.nimble.collaboration.config.EProject;
import it.domina.nimble.collaboration.config.EResource;
import it.domina.nimble.collaboration.config.EResourceLog;
import it.domina.nimble.collaboration.exceptions.PermissionDenied;
import it.domina.nimble.collaboration.exceptions.ResourceNotFound;
import it.domina.nimble.collaboration.kafka.KafkaCfg;
import it.domina.nimble.collaboration.services.type.CollabMessageType;
import it.domina.nimble.collaboration.services.type.ReadMessageType;
import it.domina.nimble.collaboration.services.type.ReadResourceType;
import it.domina.nimble.collaboration.services.type.ResourceListType;
import it.domina.nimble.collaboration.services.type.ResourceType;

public class Connector {

	private static final Logger logger = Logger.getLogger(Connector.class);

	private EProject					project;
	private DBFilesFolder				resourcesFolder;
	private HashMap<String, EndPoint> 	activePartners;
	
	public Connector(EProject prj) throws Exception {
		this.project = prj;
		this.activePartners = new HashMap<String, EndPoint>();
		checkKafkaTopic();
		checkRepository();
		logger.log(Level.INFO, "Connector initialize");
	}

	public EProject getProject() {
		return this.project;
	}

	public String getKafkaTopic() {
		return this.project.getKafkaTopic();
	}
	
	public void addPartner(EPartner prt) throws Exception {
		if (!this.activePartners.containsKey(prt.getUserId())) {
			EndPoint ep = new EndPoint(this, prt);
			this.activePartners.put(prt.getUserId(), ep);
		}
	}
	
	public Boolean sendMessage(CollabMessageType msg, EPartner prt) throws Exception {
		EndPoint ep = this.activePartners.get(prt.getUserId());
		if (ep!=null) {
			return ep.sendMsg(msg);
		}
		return false;
	}
	
	public void readMessage(ReadMessageType params, EPartner prt) throws Exception {
		EndPoint ep = this.activePartners.get(prt.getUserId());
		if (ep!=null) {
			ep.readMsg(params);
		}
	}
	
	public Boolean saveResource(ResourceType res, EPartner prt) throws Exception {
		EndPoint ep = this.activePartners.get(prt.getUserId());
		if (ep!=null) {
			DBFilesFolder resFolder = searchFolder(res.getKey(), true);
			if (res.getType().equals(ResourceType.RESOURCE_TYPE)) {
				Integer lastVersionNumber = getLastVersionNumeber(resFolder); 
				res.setVersion(Long.valueOf(lastVersionNumber+1));
				String resMeta = "VER"+String.format("%07d", lastVersionNumber+1);
				InputStream stream = new ByteArrayInputStream(res.toString().getBytes(StandardCharsets.UTF_8));
				resFolder.addFile(stream,resMeta+".json");
			}
			else {
				res.setVersion(Long.valueOf(1));
			}
			this.project.saveResource(res, prt.getUsername());
			return true;
		}
		else {
			throw new PermissionDenied();
		}
	}
	
	public ResourceListType readResourceList(EPartner prt, ResourceListType resReference) throws Exception {
		EndPoint ep = this.activePartners.get(prt.getUserId());
		if (ep!=null) {
			List<EResource> lstRes;
			if (resReference.getName() != null) {
				EResource r = this.project.getResourceDescription(resReference.getName());
				lstRes = this.project.getResourcesList(r);
			}
			else {
				lstRes = this.project.getResourcesList(null);
			}
			for (EResource r : lstRes) {
				ResourceListType newRes = new ResourceListType(resReference, r.getName(), r.getType());
				newRes.setUser(r.getUser());
				newRes.setVersion(r.getVersion());
				newRes.setNotes(r.getNotes());
				resReference.getChildren().add(newRes);
			}
			return resReference;
		}
		else {
			throw new PermissionDenied();
		}
	}

	public ResourceListType readResourceHistory(EPartner prt, ResourceListType resReference) throws Exception {
		EndPoint ep = this.activePartners.get(prt.getUserId());
		if (ep!=null) {
			if (resReference.getName() != null) {
				EResource r = this.project.getResourceDescription(resReference.getName());
				List<EResourceLog> lstRes = r.getResourcesHistory();
				for (EResourceLog rlog : lstRes) {
					ResourceListType newRes = new ResourceListType(resReference, null, resReference.getType());
					newRes.setUser(rlog.getUser());
					newRes.setVersion(rlog.getVersion());
					newRes.setNotes(rlog.getNotes());
					resReference.getChildren().add(newRes);
				}
			}
			return resReference;
		}
		else {
			throw new PermissionDenied();
		}
	}
	
	public ResourceType deleteResource(ReadResourceType params, EPartner prt) throws Exception {
		EndPoint ep = this.activePartners.get(prt.getUserId());
		if (ep!=null) {
			DBFilesFolder resFolder = searchFolder(params.getResourceName(), false);
			if (resFolder!=null) {
				ResourceType result = null;
				Integer lastVersionNumber;
				if (params.getResourceVersion()==null) {
					lastVersionNumber = getLastVersionNumeber(resFolder);	
				}
				else {
					lastVersionNumber = params.getResourceVersion();
				}
				DBFilesFile fileJson = resFolder.getFile("VER"+String.format("%07d", lastVersionNumber)+".json");
				if (fileJson!=null) {
					String json = IOUtils.toString(fileJson.getData(), StandardCharsets.UTF_8); 
					result = ResourceType.mapJson(json);
				}
				if (params.getResourceVersion()==null) {
					resFolder.removeAllFiles();
					resFolder.delete();
					EResource r = this.project.getResourceDescription(params.getResourceName());
					if (r!=null) {
						r.remove();
					}
				}
				else {
					fileJson.delete();
					EResource r = this.project.getResourceDescription(params.getResourceName());
					if (r!=null) {
						r.removeVersionLog(lastVersionNumber);
					}
				}
				return result;
			}
			else {
				throw new ResourceNotFound();
			}
		}
		else {
			throw new PermissionDenied();
		}
	}
	
	public ResourceType readResource(ReadResourceType params, EPartner prt) throws Exception {
		EndPoint ep = this.activePartners.get(prt.getUserId());
		if (ep!=null) {
			DBFilesFolder resFolder = searchFolder(params.getResourceName(), false);
			if (resFolder!=null) {
				Integer lastVersionNumber;
				if (params.getResourceVersion()==null) {
					lastVersionNumber = getLastVersionNumeber(resFolder);	
				}
				else {
					lastVersionNumber = params.getResourceVersion();
				}
				DBFilesFile fileJson = resFolder.getFile("VER"+String.format("%07d", lastVersionNumber)+".json");
				if (fileJson!=null) {
					String json = IOUtils.toString(fileJson.getData(), StandardCharsets.UTF_8); 
					ResourceType res = ResourceType.mapJson(json);
					return res;
				}
				else {
					throw new ResourceNotFound();
				}
			}
			else {
				throw new ResourceNotFound();
			}
		}
		else {
			throw new PermissionDenied();
		}
	}
	
	public void close() {
		for (EndPoint ep : this.activePartners.values()) {
			ep.close();
		}
	}
	
	private DBFilesFolder searchFolder(String path, Boolean create)  {
		DBFilesFolder currentFolder = this.resourcesFolder;
		String[] names = path.split(ResourceType.RESOURCE_SEPARATOR); 
		for (String name : names) {
			try 
			{
				currentFolder = currentFolder.getFolder(name);
			}
			catch (Exception e) {
				if (create) {
					currentFolder = currentFolder.newFolder(name);
				}
				else {
					return null;
				}
			}
		}
		return currentFolder;
	}
	
	private Boolean checkKafkaTopic() throws Exception {
		KafkaCfg kafka = ServiceConfig.getInstance().getKafkaConfig();
		List<String> topics = kafka.getTopicsList(); 
		for (String tp : topics) {
			if (tp.equals(this.project.getKafkaTopic())) {
				return true;
			}
		}
		kafka.createTopic(this.project.getKafkaTopic());
		return true;
	}
	
	private Boolean checkRepository() throws Exception {
		DBFiles rep = ServiceConfig.getInstance().getRepository();
		try {
			this.resourcesFolder = rep.getFolder(this.project.getFolderName());
		} catch (FolderNotFound e) {
			this.resourcesFolder = rep.newFolder(this.project.getFolderName());
		}
		return true;
	}

	private Integer getLastVersionNumeber(DBFilesFolder resFolder) {
		List<DBFilesFile> files = resFolder.getFilesList();
		Integer lastNumber = 0;
		for (DBFilesFile f: files) {
			if (f.getKey().startsWith("VER")) {
				Integer ver = Integer.parseInt(f.getKey().substring(3, 10));
				if (lastNumber<ver) {
					lastNumber = ver;
				}
			}
		}
		return lastNumber;
	}
	
}
