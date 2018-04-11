using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class CollaborateType:JsonType
    {
	
        public CollaborateType() {}
    
        public CollaborateType(String token, String projectName, String url) {
    	    this.token = token;
    	    this.url = url;
    	    this.projectName = projectName;
        }
    
        public String token { get; set; }
        public String projectName { get; set; }
        public String url { get; set; }

	    public static CollaborateType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<CollaborateType>(json);
		    } catch (Exception e) {
			    return null;
		    }
	    }
    }
}
