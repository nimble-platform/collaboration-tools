using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class TokenIdType: JsonType
    {

        public TokenIdType() { }

        public TokenIdType(String token) {
    	    this.token = token;
        }

        public String token { get; set; }

	    public static TokenIdType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<TokenIdType>(json);
            }
            catch (Exception e) {
			    return null;
		    }
	    }
    }
}
