package me.mrletsplay.webinterfaceapi.webinterface.auth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WebinterfaceAccountConnection {

	private String
		connectionName,
		userID,
		userName,
		userEmail,
		userAvatar;
	
	private boolean isTemporary;
	
	public WebinterfaceAccountConnection(String connectionName, String userID, String userName, String userEmail, String userAvatar, boolean isTemporary) {
		this.connectionName = connectionName;
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userAvatar = userAvatar;
		this.isTemporary = isTemporary;
	}
	
	public WebinterfaceAccountConnection(String connectionName, String userID, String userName, String userEmail, String userAvatar) {
		this(connectionName, userID, userName, userEmail, userAvatar, false);
	}
	
	@NotNull
	public String getConnectionName() {
		return connectionName;
	}
	
	@NotNull
	public String getUserID() {
		return userID;
	}
	
	@NotNull
	public String getUserName() {
		return userName;
	}
	
	@Nullable
	public String getUserEmail() {
		return userEmail;
	}
	
	@Nullable
	public String getUserAvatar() {
		return userAvatar;
	}
	
	public boolean isTemporary() {
		return isTemporary;
	}
	
}
