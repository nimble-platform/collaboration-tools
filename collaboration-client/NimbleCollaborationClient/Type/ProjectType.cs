using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class ProjectType: JsonType
    {

        public ProjectType() { }
	
	    public ProjectType(String token, String name) {
    	    this.token = token;
    	    this.name = name;
        }

        public String token { get; set; }
        public String name { get; set; }
	    public String owner { get; set; }
        public String ownerName { get; set; }

        public static ProjectType mapJson(String json)
        {
		    try {
                return new JavaScriptSerializer().Deserialize<ProjectType>(json);
            }
            catch (Exception e)
            {
			    return null;
		    }
	    }

    }
}
