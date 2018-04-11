using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class JsonType
    {
        public string ToString()
        {
            String json = new JavaScriptSerializer().Serialize(this);
            return json;
        }

    }
}
