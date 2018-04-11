using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Script.Serialization;

namespace Nimble.Client.Type
{
    public class IdentityUserType : JsonType
    {
        private String username;
        private String firstname;
        private String lastname;
        private String email;
        private String dateOfBirth;
        private String placeOBirth;
        private String phoneNumber;
        private String userID;
        private String companyID;
        private String companyName;
        private String accessToken;
	
        public String getUsername() {
		    return this.username;
	    }

	    public String getFirstname() {
		    return this.firstname;
	    }
	    public String getLastname() {
		    return this.lastname;
	    }

	    public String getEmail() {
		    return this.email;
	    }

	    public String getDateOfBirth() {
		    return this.dateOfBirth;
	    }

	    public String getPlaceOBirth() {
		    return this.placeOBirth;
	    }

	    public String getPhoneNumber() {
		    return this.phoneNumber;
	    }

	    public String getUserID() {
		    return this.userID;
	    }

	    public String getCompanyID() {
		    return this.companyID;
	    }

	    public String getCompanyName() {
		    return this.companyName;
	    }

	    public String getAccessToken() {
		    return this.accessToken;
	    }

	    public static IdentityUserType mapJson(String json) {
		    try {
                return new JavaScriptSerializer().Deserialize<IdentityUserType>(json);
		    } catch (Exception e) {
			    return null;
		    }
	    }

    
    }

}
