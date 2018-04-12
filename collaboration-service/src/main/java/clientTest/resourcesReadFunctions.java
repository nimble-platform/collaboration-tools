package clientTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import clientTest.http.Functions;
import it.domina.nimble.collaboration.services.type.ProjectType;
import it.domina.nimble.collaboration.services.type.ResourceListType;
import it.domina.nimble.collaboration.services.type.ResourceType;

public class resourcesReadFunctions {

	public static void main(String[] args) {
		try {
			
			/*

			ResourceListType testToString = new ResourceListType("TOKEN", "PROJECT_NAME","TEST");
			System.out.println(testToString.toString()); 
			
			ResourceListType resGroup1 = new ResourceListType(testToString,"GROUP1");
			System.out.println(resGroup1.toString());
			
			testToString.getChildren().add(resGroup1);
			System.out.println(testToString.toString());
			
			ResourceType res2 = new ResourceType("PPE", "IMG_ART333");
			File imgPath2 = new File("C:\\Users\\ggariddi\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_42_1.png");

			InputStream source = new FileInputStream(imgPath2);
			InputStream dest = IOUtils.toBufferedInputStream(source);
			source.close();
			source = null;
			byte[] data2 = IOUtils.toByteArray(dest);
			res2.setResource(new BytesType(data2));
			
			String txtValue = res2.toString();
			
			ResourceType resSX = ResourceType.mapJson(txtValue);			
			File  imgPathOut = new File("C:\\Users\\ggariddi\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_42_1_out.png");
			OutputStream outputStream = new FileOutputStream(imgPathOut);
			outputStream.write(resSX.getResource().getBytes());
			outputStream.close();
			outputStream = null;
			System.gc();
			*/
			
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
			// Get Resource
			
			if (idToken == null) return; 
			if (projectName == null) return;

			
			ResourceListType lstRes = Functions.getResourceList(idToken, projectName);
			for (ResourceListType res : lstRes.getChildren()) {
				if (res.getName().equals("IMG_ART333")) {
					ResourceType risorsa = Functions.getResources(idToken, projectName, res.getName());
					File  imgPathOut = new File("C:\\Users\\ggariddi\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_42_1_"+risorsa.getVersion().toString()+".png");
					OutputStream outputStream = new FileOutputStream(imgPathOut);
					outputStream.write(risorsa.getResource().getBytes());
					outputStream.close();
					outputStream = null;
					System.gc();
					

					risorsa = Functions.getResources(idToken, projectName, res.getName(), 1);
					imgPathOut = new File("C:\\Users\\ggariddi\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_42_1_"+risorsa.getVersion().toString()+".png");
					outputStream = new FileOutputStream(imgPathOut);
					outputStream.write(risorsa.getResource().getBytes());
					outputStream.close();
					outputStream = null;
					System.gc();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
