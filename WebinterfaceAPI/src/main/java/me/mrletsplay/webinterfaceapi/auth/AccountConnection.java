package me.mrletsplay.webinterfaceapi.auth;

public class AccountConnection {

	private String
		connectionName,
		userID,
		userName,
		userEmail,
		userAvatar;

	public AccountConnection(String connectionName, String userID, String userName, String userEmail, String userAvatar) {
		this.connectionName = connectionName;
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userAvatar = userAvatar;
	}

	public String getConnectionName() {
		return connectionName;
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
