package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.io.File;

import me.mrletsplay.webinterfaceapi.http.header.HttpURLPath;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.webinterface.config.WebinterfaceConfig;

public interface WebinterfaceAuthMethod {
	
	public String getID();
	
	public String getName();
	
	public void handleAuthRequest();
	
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException;
	
	public default HttpURLPath getAuthResponseUrl() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		
		WebinterfaceConfig cfg = Webinterface.getConfig();
		
		String override = cfg.getOverride("auth." + getID() + ".redirect-url", String.class);
		if(override != null) return HttpURLPath.of(override);
		
		String host;
		if(cfg.getSetting(DefaultSettings.USE_CLIENT_HOST)) {
			host = c.getClientHeader().getFields().getFieldValue("Host");
		}else {
			int port = c.isConnectionSecure() ? cfg.getSetting(DefaultSettings.HTTPS_PORT) : cfg.getSetting(DefaultSettings.HTTP_PORT);
			String hostname = c.isConnectionSecure() ? cfg.getSetting(DefaultSettings.HTTPS_HOST) : cfg.getSetting(DefaultSettings.HTTP_HOST);
			host = hostname + ":" + port;
		}
		
		return HttpURLPath.of((c.isConnectionSecure() ? "https" : "http") + "://" + host + "/auth/" + getID() + "/response"); // TODO: verify host
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
