package me.mrletsplay.webinterfaceapi.webinterface.auth;

import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;

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

}
