using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class BytesType: JsonType
    {

        public BytesType(byte[] bytes) {
            this.bytes = bytes;
        }

        public byte[] bytes { get; set;}

        public static BytesType mapJson(String json)
        {
            try
            {
                return new JavaScriptSerializer().Deserialize<BytesType>(json);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
}
