using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class ReadResourceType: JsonType
    {

        public ReadResourceType() { }
    
        public ReadResourceType(String token, String projectID, String name):this(token, projectID, name, 0) { 
    	    
        }

        public ReadResourceType(String token, String projectName, String name, int? ver)
        { 
    	    this.token = token;
    	    this.projectName = projectName;
    	    this.resourceName = name;
    	    this.resourceVersion = ver;
        }

        public String token {get; set;}
        public String projectName { get; set; }
        public String resourceName { get; set; }
        public int? resourceVersion { get; set; }

        public static ReadResourceType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<ReadResourceType>(json);
		    } catch (Exception e) {
			    return null;
		    }
	    }


    }
}
