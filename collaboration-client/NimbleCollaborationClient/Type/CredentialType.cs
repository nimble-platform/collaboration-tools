using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class CredentialType : JsonType
    {

        public CredentialType(String username, String password) {
    	    this.username = username;
    	    this.password = password;
        }

        public String username { get; set; }
        public String password { get; set; }

	    public static CredentialType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<CredentialType>(json);
            }
            catch (Exception e)
            {
			    return null;
		    }
	    }

    }
}
