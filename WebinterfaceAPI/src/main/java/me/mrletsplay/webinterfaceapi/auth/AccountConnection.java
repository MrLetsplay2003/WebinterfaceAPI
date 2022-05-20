package me.mrletsplay.webinterfaceapi.auth;

public class AccountConnection {

	private String
		connectionName,
		userID,
		userName,
		userEmail,
		userAvatar;
	
	private boolean isTemporary;
	
	public AccountConnection(String connectionName, String userID, String userName, String userEmail, String userAvatar, boolean isTemporary) {
		this.connectionName = connectionName;
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userAvatar = userAvatar;
		this.isTemporary = isTemporary;
	}
	
	public AccountConnection(String connectionName, String userID, String userName, String userEmail, String userAvatar) {
		this(connectionName, userID, userName, userEmail, userAvatar, false);
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
	
	public boolean isTemporary() {
		return isTemporary;
	}
	
}
