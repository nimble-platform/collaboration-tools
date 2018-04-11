using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class CollabMessageType: JsonType
    {

        public CollabMessageType() {
    	    this.uniqueID = System.Guid.NewGuid().ToString();
            contents = new Dictionary<String, String>();  
        }
    
        public CollabMessageType(String projectName) :this(projectName, null) { }

        public CollabMessageType(String projectName, String title) : this() { 
    	    this.projectName = projectName;
    	    this.title = title;
        }

        public String uniqueID { get; set; }
   	    public String projectName { get; set; }
   	    public String title { get; set; }
   	    public Dictionary<String, String> contents { get; set; }

        public String encodeContext() {
    	    String output = null;
    	    foreach (String key in this.contents.Keys) {
			    if (output== null) {
	    		    output += key + "=" +  this.contents[key];
			    }
			    else {
	    		    output += ";" + key + "=" + this.contents[key];
			    }
		    }
    	    return output;
        }
    
        public static CollabMessageType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<CollabMessageType>(json);
            }
            catch (Exception e)
            {
			    return null;
		    }
	    }

    }
}
