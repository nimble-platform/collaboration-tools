using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nimble.Client.Exceptions
{
    public class SubscriptionRequired : Exception
    {
        
        public const String ERRCODE = "SUBSCRIPTION_REQUIRED";
	
	    public SubscriptionRequired() : base(ERRCODE) { }

    }
}
