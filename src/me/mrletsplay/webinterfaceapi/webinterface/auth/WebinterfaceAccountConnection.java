package me.mrletsplay.webinterfaceapi.webinterface.auth;

public class WebinterfaceAccountConnection {

	private String authMethod, userID, userName, userEmail, userAvatar;

	public WebinterfaceAccountConnection(String authMethod, String userID, String userName, String userEmail, String userAvatar) {
		this.authMethod = authMethod;
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userAvatar = userAvatar;
	}
	
	public String getAuthMethod() {
		return authMethod;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public String getUserAvatar() {
		return userAvatar;
	}
	
}
