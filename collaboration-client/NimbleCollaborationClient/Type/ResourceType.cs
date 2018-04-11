using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class ResourceType: JsonType
    {

        public const char RESOURCE_SEPARATOR = '/';

        public const String GROUP_TYPE = "GROUP_TYPE";
	    public const String RESOURCE_TYPE = "RESOURCE_TYPE";

        public ResourceType() { }

        public ResourceType(String projectID, String name, String type) : this(projectID, name, type, null) { }

        public ResourceType(String projectName, String key, String type, String ext) { 
            this.projectName = projectName;
            this.type = type;
            this.key = key;
            this.ext = ext;
            String[] lstBack = key.Split(ResourceType.RESOURCE_SEPARATOR);
            this.name = lstBack[lstBack.Count() - 1];
        }

        public String projectName { get; set; }
        public String key { get; set; }
        public String name { get; set; }
        public String type { get; set; }
        public String ext { get; set; }
        public Int32 version { get; set; }
        public String resource { get; set; }

        public static ResourceType mapJson(String json) {
	        try {
                return new JavaScriptSerializer().Deserialize<ResourceType>(json);
	        } catch (Exception e) {
		        return null;
	        }
        }


    }
}
