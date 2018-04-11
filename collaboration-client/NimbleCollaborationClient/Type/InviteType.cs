using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class InviteType: JsonType
    {

        public InviteType() { }
	
	    public InviteType(String token, String projectName, String userID) {
    	    this.token = token;
    	    this.projectName = projectName;
    	    this.userID = userID;
        }

        public String token { get; set; }
        public String projectName { get; set; }
	    public String userID { get; set; }

	    public static InviteType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<InviteType>(json);
		    } catch (Exception e) {
			    return null;
		    }
	    }
    }
}
