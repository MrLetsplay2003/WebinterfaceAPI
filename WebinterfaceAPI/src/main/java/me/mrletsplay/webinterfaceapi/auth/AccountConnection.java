package me.mrletsplay.webinterfaceapi.auth;

public class AccountConnection {

	private String
		connectionName,
		userID,
		userName,
		userEmail,
		userAvatar;

	private Runnable postRegistrationCallback;

	public AccountConnection(String connectionName, String userID, String userName, String userEmail, String userAvatar, Runnable postRegistrationCallback) {
		this.connectionName = connectionName;
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userAvatar = userAvatar;
		this.postRegistrationCallback = postRegistrationCallback;
	}

	public AccountConnection(String connectionName, String userID, String userName, String userEmail, String userAvatar) {
		this(connectionName, userID, userName, userEmail, userAvatar, null);
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

	/**
	 * Allows auth methods to run code after the registration was completed
	 */
	public void runPostRegistrationCallback() {
		if(postRegistrationCallback != null) postRegistrationCallback.run();
	}

}
