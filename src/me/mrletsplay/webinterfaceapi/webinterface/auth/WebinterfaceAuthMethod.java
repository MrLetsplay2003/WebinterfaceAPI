package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.io.File;

import me.mrletsplay.webinterfaceapi.http.header.HttpURLPath;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public interface WebinterfaceAuthMethod {
	
	public String getID();
	
	public String getName();
	
	public void handleAuthRequest();
	
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException;
	
	public default HttpURLPath getAuthResponseUrl() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String host = c.getClientHeader().getFields().getFieldValue("Host");
		return HttpURLPath.of("http://" + host + "/auth/" + getID() + "/response"); // TODO: verify host
	}
	
	public default HttpURLPath getPostAuthRedirectURL() {
		return HttpURLPath.of(HttpRequestContext.getCurrentContext().getClientHeader().getPath().getQueryParameterValue("from", "/"));
	}
	
	public default File getConfigurationDirectory() {
		return new File(Webinterface.getConfigurationDirectory(), "auth/" + getID() + "/");
	}
	
	public default boolean isAvailable() {
		return true;
	}

}
