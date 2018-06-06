# Collaboration-Service
Collaboration-Service tool for the Nimble Platform.

## Development
The Collaboration-Service, server side, is developed in Java and run under Tomcat.

### Develpement with Eclipse
For development purpose, install a copy of Eclipse (Oxigen Version was used). 
Once the Eclipse is ready, install on it in the a copy of Tomcat 7.

Clone the repository or download the entire project, import the project in the Eclipse and attach the solution to Tomcat.

Install a mysql database, create a new database and import the database structure from /src/main/resources/NimbleCollaborationServiceDB.sql

Finally in the /src/main/resources you can find all config file to edit in order to setup the file.

## Deployment

### Local / Non-cloud
Deploy under tomcat server.

# collaboration-client
collaboration-client tool for the Nimble Platform.

## Development
Is a client library in Dot.NET to interact with server side.

### Develpement with Visual Studio
For development purpose, install a copy of Visual Studio (VS2010 was used). 
Once the IDE is ready, open the solution.

Under "CollaborationTool.cs" change the URL to point to the correct end-point Collaboration-Service
public static string baseURL = "http://localhost:8081/collaboration-service";

## Deployment

### Local 
Include the dll in the dependencies of a project. 


----
The project leading to this application has received funding from the European Union’s Horizon 2020 research and innovation programme under grant agreement No 723810.
