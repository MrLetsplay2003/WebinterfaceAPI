package me.mrletsplay.webinterfaceapi.webinterface.auth;

public class WebinterfaceAccountData {

	private String userID, userName, userEmail, userAvatar;

	public WebinterfaceAccountData(String userID, String userName, String userEmail, String userAvatar) {
		this.userID = userID;
		this.userEmail = userEmail;
		this.userAvatar = userAvatar;
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
