using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class ProjectListType: JsonType
    {

        public ProjectListType() { }
   	
        public List<ProjectType> getProjectList() {
            if (this.projectList == null)
            {
                this.projectList = new List<ProjectType>();
    	    }
            return this.projectList;
        }

        public List<ProjectType> projectList { get; set; }

	    public static ProjectListType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<ProjectListType>(json);
		    } catch (Exception e) {
			    return null;
		    }
	    }
    }
}
