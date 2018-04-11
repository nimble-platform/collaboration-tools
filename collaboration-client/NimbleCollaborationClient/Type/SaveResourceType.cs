using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class SaveResourceType : JsonType
    {
        public SaveResourceType() { 
            this.resources = new List<ResourceType>();  
        }
    
        public SaveResourceType(String token): this(token, null){ }

        public SaveResourceType(String token, ResourceType res) { 
    	    this.token = token;
            this.resources = new List<ResourceType>();
            if (res != null)
            {
                this.resources.Add(res);
            }
        }

        public String token { get; set; }
        public List<ResourceType> resources { get; set; }
	
        public static SaveResourceType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<SaveResourceType>(json);
            }
            catch (Exception e)
            {
			    return null;
		    }
	    }

    }
}
