package me.mrletsplay.webinterfaceapi.webinterface.auth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WebinterfaceAccountConnection {

	private String authMethod, userID, userName, userEmail, userAvatar;
	private boolean isTemporary;
	
	public WebinterfaceAccountConnection(String authMethod, String userID, String userName, String userEmail, String userAvatar, boolean isTemporary) {
		this.authMethod = authMethod;
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userAvatar = userAvatar;
		this.isTemporary = isTemporary;
	}
	
	public WebinterfaceAccountConnection(String authMethod, String userID, String userName, String userEmail, String userAvatar) {
		this(authMethod, userID, userName, userEmail, userAvatar, false);
	}
	
	@NotNull
	public String getAuthMethod() {
		return authMethod;
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
