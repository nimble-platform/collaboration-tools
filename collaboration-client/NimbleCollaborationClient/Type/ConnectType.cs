using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class ConnectType : JsonType
    {

        public ConnectType() { }
	
	    public ConnectType(String token, String inviteID) {
    	    this.token = token;
    	    this.inviteID = inviteID;
        }

        public String token  { get; set; }
        public String inviteID { get; set; }

	    public static ConnectType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<ConnectType>(json);
            }
            catch (Exception e)
            {
			    return null;
		    }
	    }
    }
}
