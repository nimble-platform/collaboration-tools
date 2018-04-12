package clientTest;

import java.util.List;

import clientTest.http.Functions;
import it.domina.nimble.collaboration.exceptions.SubscriptionRequired;
import it.domina.nimble.collaboration.services.type.ProjectType;

public class initFunctions {

	public static void main(String[] args) {
		try {
			
			String projectName = null;
			String inviteID = null;
			
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
				} catch (SubscriptionRequired e) {
					Functions.subscribe(idToken);
					if (Functions.createProject(idToken, "CAD1")) {
						projectName = "CAD1";
					}
					else {
						projectName = null;
					}
				} catch (Exception e) {
					projectName = null;
				}

				if (projectName == null) {
					if (Functions.createProject(idToken, "CAD1")) {
						projectName = "CAD1";
					}
				}
				else {
					Functions.startCollaborate(idToken, projectName);
				}
				
				if (projectName != null) {
					inviteID = Functions.sendInvite(idToken, projectName, "3108");
				}
					
			}
			
			String idTokenCustomer = Functions.login("username", "password");
			if (idTokenCustomer!=null) {
				
				Boolean find = false;
				try {
					List<ProjectType> prjList = Functions.getProjectList(idTokenCustomer);
					for (ProjectType prj : prjList) {
						if (prj.getName().equals(projectName)) {
							find = true;
							break;
						}
					}
				} catch (SubscriptionRequired e) {
					Functions.subscribe(idTokenCustomer);
				} catch (Exception e) {
					find = false;
				}
				
				//Subscribe to Project
				if (!find) {
					if (inviteID!=null) {
						projectName = Functions.connectProject(idTokenCustomer, inviteID);
						find = true;
					}
				}

				//Start Collaboration
				if (find) {
					Functions.startCollaborate(idTokenCustomer, projectName);
				}
			}
			
			
			// Setup done

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
