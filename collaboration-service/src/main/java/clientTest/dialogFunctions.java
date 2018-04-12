package clientTest;

import java.util.List;

import clientTest.http.Functions;
import it.domina.nimble.collaboration.services.type.CollabMessageType;
import it.domina.nimble.collaboration.services.type.ProjectType;
import it.domina.nimble.collaboration.services.type.ReadMessageType;
import it.domina.nimble.collaboration.services.type.SendMessageType;

public class dialogFunctions {

	public static void main(String[] args) {
		try {
			
			String projectName = null;
			
			String idToken = Functions.login("username", "password");
			if (idToken!=null) {
				
				//Open projects list
				try {
					List<ProjectType> prjList = Functions.getProjectList(idToken);
					for (ProjectType prj : prjList) {
						if (prj.getName().equalsIgnoreCase("CAD1")) {
							projectName = prj.getName();
							break;
						}
					}
				} catch (Exception e) {
					projectName = null;
				}

			}
			
			Functions.startCollaborate(idToken, projectName);

			// Setup done
			// ----------------------
			// Test message excange
			
			if (idToken == null) return; 
			//if (idTokenCustomer == null) return;
			if (projectName == null) return;

			/*
			ReadMessageType msgReturn =  Functions.readMessages(idToken, myProjectID);
			for (CollabMessageType m : msgReturn.getMessages()) {
				System.out.println(m.getUniqueID()+"-"+m.getProjectID()+"-"+m.getTitle());
			}
			*/

			CollabMessageType msg = new CollabMessageType(projectName, "PROVA");
			msg.setContent("KEY1", "VAL1");
			msg.setContent("KEY2", "VAL2");
			msg.setContent("KEY3", "VAL3");
			SendMessageType msg_out = new SendMessageType(idToken, msg);
			Functions.sendMessage(msg_out);

			ReadMessageType msgReturn =  Functions.readMessages(idToken, projectName);
			for (CollabMessageType m : msgReturn.getMessages()) {
				System.out.println(m.getUniqueID()+"-"+m.getProjectName()+"-"+m.getTitle());
			}
			/*
			msgReturn =  Functions.readMessages(idTokenCustomer, myProjectID);
			for (CollabMessageType m : msgReturn.getMessages()) {
				System.out.println(m.getUniqueID()+"-"+msg.getProjectID()+"-"+msg.getTitle());
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
