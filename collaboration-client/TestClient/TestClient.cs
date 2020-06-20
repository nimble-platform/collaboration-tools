using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using System.Diagnostics;
using Nimble.Client;
using Nimble.Client.Type;
using Nimble.Client.Exceptions;

class TestClient
{
    static void Main(string[] args)
    {
        //initFunction();
        //dialogFunctions();
        //saveResourceFunctions();
        readResourceFunctions();
    }

    private static void saveResourceFunctions()
    {
	try {
			
		String projectName = null;
			
		String idToken = CollaborationTool.login("", "");
		if (idToken!=null) {
				
			//Open projects list
			try {
				List<ProjectType> prjList = CollaborationTool.getProjectList(idToken);
				foreach (ProjectType prj in prjList) {
					if (prj.name.Equals("CAD1")) {
						projectName = prj.name;
						break;
					}
				}
			} catch (Exception e) {
				projectName = null;
			}

		}
			
		CollaborationTool.startCollaborate(idToken, projectName);
		
		// Setup done
		// ----------------------
		// Test save resource
			
		if (idToken == null) return; 
		if (projectName == null) return;

		ResourceType res = new ResourceType(projectName, "IMG_ART333", "png");
        byte[] data2 = File.ReadAllBytes("C:\\Users\\ggariddi\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_50_4.png");
        res.resource = Convert.ToBase64String(data2);
        //res.resource = (new BytesType(data2));
        SaveResourceType resToSave = new SaveResourceType(idToken);
		resToSave.resources.Add(res);
			
        Debug.Print(resToSave.ToString());
        CollaborationTool.saveResources(resToSave);

	} catch (Exception e) {
		logError(e);
	}

    }

    private static void readResourceFunctions()
    {
        
		try {
			
			String projectName = null;
			
			String idToken = CollaborationTool.login("", "");
			if (idToken!=null) {
				
				//Open projects list
				try {
					List<ProjectType> prjList = CollaborationTool.getProjectList(idToken);
					foreach (ProjectType prj in prjList) {
						if (prj.name.Equals("CAD1")) {
							projectName = prj.name;
							break;
						}
					}
				} catch (Exception e) {
					projectName = null;
				}

			}
			
			CollaborationTool.startCollaborate(idToken, projectName);

			
			// Setup done
			// ----------------------
			// Get Resource
			
			if (idToken == null) return; 
			if (projectName == null) return;

			
			ResourceListType lstRes = CollaborationTool.getResourceList(idToken, projectName);
			foreach (String resName in lstRes.getResources()) {
				if (resName.Equals("IMG_ART333")) {
                    ResourceType res = CollaborationTool.getResources(idToken, projectName, resName);
                    byte[] data = Convert.FromBase64String(res.resource);
                    File.WriteAllBytes("C:\\Users\\ggariddi\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_42_1_" + res.version.ToString() + ".png", data);

                    res = CollaborationTool.getResources(idToken, projectName, resName, 1);
                    data = Convert.FromBase64String(res.resource);
                    File.WriteAllBytes("C:\\Users\\ggariddi\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_42_1_" + res.version.ToString() + ".png", data);
				}
			}

		} catch (Exception e) {
            logError(e);
		}
        
    }


    private static void dialogFunctions()
    {
        try{
            String projectName = null;
			
			String idToken = CollaborationTool.login("", "");
			if (idToken!=null) {
				
				//Open projects list
				try {
					List<ProjectType> prjList = CollaborationTool.getProjectList(idToken);
					foreach (ProjectType prj in prjList) {
						if (prj.name.Equals("CAD1")) {
							projectName = prj.name;
							break;
						}
					}
				} catch (Exception e) {
					projectName = null;
				}

			}
			
			CollaborationTool.startCollaborate(idToken, projectName);

			// Setup done
			// ----------------------
			// Test message excange
			
			if (idToken == null) return; 
			if (projectName == null) return;

			CollabMessageType msg = new CollabMessageType(projectName, "PROVA");
			msg.contents.Add("KEY1", "VAL1");
			msg.contents.Add("KEY2", "VAL2");
			msg.contents.Add("KEY3", "VAL3");
			SendMessageType msg_out = new SendMessageType(idToken, msg);
			CollaborationTool.sendMessage(msg_out);

			ReadMessageType msgReturn =  CollaborationTool.readMessages(idToken, projectName);
			foreach (CollabMessageType m in msgReturn.messages) {
                Debug.Print(m.uniqueID+"-"+m.projectName+"-"+m.title);
			}
		} catch (Exception e) {
			logError(e);
		}

    }

    private static void initFunction()
    {
        try
        {

            String projectName = null;
            String inviteID = null;

            String idToken = CollaborationTool.login("", "");
            if (idToken != null)
            {

                Debug.Print(idToken);

                //Open projects list
				try {
					List<ProjectType> prjList = CollaborationTool.getProjectList(idToken);
					foreach (ProjectType prj in prjList) {
						if (prj.name.Equals("CAD1")) {
							projectName = prj.name;
							break;
						}
					}
				} catch (SubscriptionRequired e) {
					CollaborationTool.subscribe(idToken);
					if (CollaborationTool.createProject(idToken, "CAD1")) {
						projectName = "CAD1";
					}
					else {
						projectName = null;
					}
				} catch (Exception e) {
					projectName = null;
				}

				if (projectName == null) {
					if (CollaborationTool.createProject(idToken, "CAD1")) {
						projectName = "CAD1";
					}
				}
				else {
					CollaborationTool.startCollaborate(idToken, projectName);
				}
				
				if (projectName != null) {
					inviteID = CollaborationTool.sendInvite(idToken, projectName, "3108");
				}

            }

            String idTokenCustomer = CollaborationTool.login("", "");
            if (idTokenCustomer!=null) {
				
                Boolean find = false;
                try {
                    List<ProjectType> prjList = CollaborationTool.getProjectList(idTokenCustomer);
                    foreach (ProjectType prj in prjList) {
                        if (prj.name.Equals(projectName)) {
                            find = true;
                            break;
                        }
                    }
                } catch (SubscriptionRequired e) {
                    CollaborationTool.subscribe(idTokenCustomer);
                } catch (Exception e) {
                    find = false;
                }
				
                //Subscribe to Project
                if (!find) {
                    if (inviteID!=null) {
                        projectName = CollaborationTool.connectProject(idTokenCustomer, inviteID);
                        find = true;
                    }
                }

                //Start Collaboration
                if (find) {
                    CollaborationTool.startCollaborate(idTokenCustomer, projectName);
                }
            }
            // Setup done

        }
        catch (Exception e)
        {
            logError(e);
        }

    }

        
    private static void logError(Exception ex)
    {
        string ls_Out = ex.StackTrace;
        Trace.WriteLine(ex.Message + "\n Trace=" + ls_Out);
    }

}
    
