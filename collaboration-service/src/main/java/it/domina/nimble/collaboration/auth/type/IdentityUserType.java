package it.domina.nimble.collaboration.auth.type;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import it.domina.nimble.utils.JsonType;

public class IdentityUserType extends JsonType  {

	private static final Logger logger = Logger.getLogger(IdentityUserType.class);

	/*
	 * 
	{
	   "username": "ggariddi@dobi.it",
	   "firstname": "Gabriele",
	   "lastname": "Gariddi",
	   "email": "ggariddi@dobi.it",
	   "dateOfBirth": null,
	   "placeOBirth": null,
	   "phoneNumber": null,
	   "userID": 2045,
	   "companyID": "2047",
	   "companyName": "Domina srl",
	   "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ6cFp2LVlNTlJfRURaeEJDN0tRTmw0VEVqektpcUJnNzJhY0JHZjlCbVFVIn0.eyJqdGkiOiIwYWMzZTJmYi05YWUxLTQ1ZjMtOThmNC1iMjFhODI2YTgzN2MiLCJleHAiOjE1MTc4MzI0NTksIm5iZiI6MCwiaWF0IjoxNTE3ODI4ODU5LCJpc3MiOiJodHRwOi8va2V5Y2xvYWs6ODA4MC9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJuaW1ibGVfY2xpZW50Iiwic3ViIjoiY2E2ZmRjOGMtNTYyMi00ZjU0LTgzZWYtYWIwOTIxOGNjN2JiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoibmltYmxlX2NsaWVudCIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjZiOTYwZTljLTQ5ZDYtNDk3Ny1hYWNjLTk0ZTJmOGIxZjJhNiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOltdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsibGVnYWxfcmVwcmVzZW50YXRpdmUiLCJuaW1ibGVfdXNlciIsImluaXRpYWxfcmVwcmVzZW50YXRpdmUiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sIm5hbWUiOiJHYWJyaWVsZSBHYXJpZGRpIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZ2dhcmlkZGlAZG9iaS5pdCIsImdpdmVuX25hbWUiOiJHYWJyaWVsZSIsImZhbWlseV9uYW1lIjoiR2FyaWRkaSIsImVtYWlsIjoiZ2dhcmlkZGlAZG9iaS5pdCJ9.SA2CWrpAPD5k4APgXzSiPjQh1tYVCkNOXu-dVOj03Zp3WcMYp6a9yOHuZf74QKz3UByLTkUgdTmrHTjCQTtkzDRaG1CuFCqAtc5ybEm5QyeA5KXR1hNRNSlxyHC9KBIZQFUBr5hULWg7_CahLlDaD8NuKhXuWUByAMd6TXV2zztTXIAQmtHK4bORvuleU424ns39kn7J4Bwsy-OjKjTufqan9ZS3012LLxvFvYRc1tdBLCu-bVMFJGRbb7OshgcJtnbNbf5FZdy8JfUBNoPlnkOXzLq3gqsdDl7oX5DOWaxGsj4WM3oGZ1UhY_Mzslfo-w4px1ngO1aTXfzHZ6bBVA"
	}
	 */

	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String dateOfBirth;
	private String placeOBirth;
	private String phoneNumber;
	private String userID;
	private String companyID;
	private CompanyNameType companyName;
	private String accessToken;
	
	public IdentityUserType(){}

	public IdentityUserType(String userID, String token){
		this.userID = userID;
		this.username = userID;
		this.email = userID;
		this.accessToken = token;
	}

	
	public String getUsername() {
		return username;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public String getEmail() {
		return email;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public String getPlaceOBirth() {
		return placeOBirth;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getUserID() {
		return userID;
	}
	public String getCompanyID() {
		return companyID;
	}
	public CompanyNameType getCompanyName() {
		return companyName;
	}
	public String getAccessToken() {
		return accessToken;
	}

	public static IdentityUserType mapJson(String json) {
		try {
            return (new Gson()).fromJson(json, IdentityUserType.class);
		} catch (Exception e) {
			logger.log(Level.INFO, e);
			return null;
		}
	}

}
