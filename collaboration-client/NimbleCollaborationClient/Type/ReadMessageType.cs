using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class ReadMessageType : JsonType
    {
    
        public ReadMessageType() 
        { 
            this.messages = new List<CollabMessageType>();  
        }
    
        public ReadMessageType(String token, String projectName) { 
    	    this.token = token;
    	    this.projectName = projectName;
            this.messages = new List<CollabMessageType>();  
        }

        public String token { get; set; }
   	    public String projectName { get; set; }
   	    public List<CollabMessageType> messages { get; set; } 

        public static ReadMessageType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<ReadMessageType>(json);
            }
            catch (Exception e)
            {
			    return null;
		    }
	    }
    }
}
