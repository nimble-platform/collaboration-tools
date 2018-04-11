using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class SendMessageType: JsonType
    {
        
        public SendMessageType() {
            messages = new List<CollabMessageType>();
        }
    
        public SendMessageType(String token) : this() { 
    	    this.token = token;
        }

        public SendMessageType(String token, CollabMessageType msg) : this()
        { 
    	    this.token = token;
    	    if (msg!=null) {
    		    this.messages.Add(msg);
    	    }
        }

        public String token { get; set; }
   	    public List<CollabMessageType> messages  { get; set; }  

        public static SendMessageType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<SendMessageType>(json);
            }
            catch (Exception e)
            {
			    return null;
		    }
	    }
    }
}
