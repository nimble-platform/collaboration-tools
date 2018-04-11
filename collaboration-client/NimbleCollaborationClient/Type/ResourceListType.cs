using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class ResourceListType:JsonType
    {

       	public ResourceListType() {}

        public ResourceListType(String token, String project, String name)
        {
            this.token = token;
            this.projectName = project;
            this.name = name;
        }

        public List<ResourceListType> getChildren()
        {
            if (this.children == null)
            {
                this.children = new List<ResourceListType>();
    	    }
            return this.children;
        }

        public String token { get; set; }
        public String projectName { get; set; }
        public String name { get; set; }
        public String type { get; set; }
        public int version { get; set; }
        public String user { get; set; }
        public List<ResourceListType> children { get; set; }

	    public static ResourceListType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<ResourceListType>(json);
		    } catch (Exception e) {
			    return null;
		    }
	    }
    }
}
