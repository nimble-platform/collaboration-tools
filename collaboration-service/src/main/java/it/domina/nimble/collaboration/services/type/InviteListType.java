package it.domina.nimble.collaboration.services.type;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.domina.nimble.collaboration.config.EInvite;
import it.domina.nimble.utils.JsonType;

public class InviteListType extends JsonType {

    private static final Logger logger = Logger.getLogger(InviteListType.class);

   	private List<InviteType> invites;
	
   	protected InviteListType() {}
   	
   	public InviteListType(String token, List<EInvite> lst) {
    	this.invites = new Vector<InviteType>();
    	for (EInvite inv : lst) {
			this.invites.add(new InviteType(token, inv));
		}
    }

    public List<InviteType> getInviteList() {
    	if (this.invites== null) {
        	this.invites = new Vector<InviteType>();
    	}
    	return this.invites;
    }

	public static InviteListType mapJson(String json) {
		try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, InviteListType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

}
