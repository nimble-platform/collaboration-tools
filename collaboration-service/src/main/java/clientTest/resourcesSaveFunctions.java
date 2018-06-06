package clientTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import clientTest.http.Functions;
import it.domina.nimble.collaboration.services.type.BytesType;
import it.domina.nimble.collaboration.services.type.ProjectType;
import it.domina.nimble.collaboration.services.type.ResourceType;
import it.domina.nimble.collaboration.services.type.SaveResourceType;

public class resourcesSaveFunctions {

	public static void main(String[] args) {
		try {
			
			/*
			ResourceType res2 = new ResourceType("PPE", "IMG_ART333");
			File imgPath2 = new File("C:\\Users\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_42_1.png");

			InputStream source = new FileInputStream(imgPath2);
			InputStream dest = IOUtils.toBufferedInputStream(source);
			source.close();
			source = null;
			byte[] data2 = IOUtils.toByteArray(dest);
			res2.setResource(new BytesType(data2));
			
			String txtValue = res 2.toString();
			
			ResourceType resSX = ResourceType.mapJson(txtValue);			
			File  imgPathOut = new File("C:\\Users\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1869_42_1_out.png");
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
			// Test save resource
			
			if (idToken == null) return; 
			if (projectName == null) return;

			ResourceType res = new ResourceType(projectName, "IMG_ART333", ResourceType.RESOURCE_TYPE, "png");
			File imgPath = new File("C:\\Users\\CAD_IMAGE\\CAD_IMAGE\\1869_50_4.png");
			InputStream source = new FileInputStream(imgPath);
			InputStream dest = IOUtils.toBufferedInputStream(source);
			source.close();
			source = null;
			byte[] data2 = IOUtils.toByteArray(dest);
			res.setResource(new BytesType(data2));
			SaveResourceType resToSave = new SaveResourceType(idToken,res);
			System.out.println(resToSave);
			Functions.saveResources(resToSave);

			res = new ResourceType(projectName, "IMG_ART333", ResourceType.RESOURCE_TYPE, "png");
			imgPath = new File("C:\\Users\\Pictures\\CAD_IMAGE\\CAD_IMAGE\\1917_37_1.png");
			source = new FileInputStream(imgPath);
			dest = IOUtils.toBufferedInputStream(source);
			source.close();
			source = null;
			data2 = IOUtils.toByteArray(dest);
			res.setResource(new BytesType(data2));
			resToSave = new SaveResourceType(idToken,res);
			System.out.println(resToSave);
			
			Functions.saveResources(resToSave);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
