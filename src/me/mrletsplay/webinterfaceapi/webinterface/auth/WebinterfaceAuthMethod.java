package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.io.File;

import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public interface WebinterfaceAuthMethod {
	
	public String getID();
	
	public String getName();
	
	public void handleAuthRequest();
	
	public WebinterfaceAccountData handleAuthResponse() throws AuthException;
	
	public default String getAuthResponseUrl() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String host = c.getClientHeader().getFields().getFieldValue("Host");
		return "http://" + host + "/auth/" + getID() + "/response";
	}
	
	public default File getConfigurationDirectory() {
		return new File(Webinterface.getConfigurationDirectory(), "auth/" + getID() + "/");
	}
	
	public default boolean isAvailable() {
		return true;
	}

}
