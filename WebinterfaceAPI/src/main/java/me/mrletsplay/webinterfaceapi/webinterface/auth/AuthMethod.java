package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.io.File;

import me.mrletsplay.simplehttpserver.http.header.HttpURLPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.Config;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public interface AuthMethod {

	public String getID();

	public String getName();

	public void handleAuthRequest();

	public AccountConnection handleAuthResponse() throws AuthException;

	public default HttpURLPath getAuthResponseUrl() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();

		Config cfg = Webinterface.getConfig();

		String override = cfg.getOverride("auth." + getID() + ".redirect-url", String.class);
		if(override != null) return HttpURLPath.of(override);

		String host;
		if(cfg.getSetting(DefaultSettings.USE_CLIENT_HOST)) {
			host = c.getClientHeader().getFields().getFirst("Host");
		}else {
			int port = c.isConnectionSecure() ? cfg.getSetting(DefaultSettings.HTTPS_PORT) : cfg.getSetting(DefaultSettings.HTTP_PORT);
			String hostname = c.isConnectionSecure() ? cfg.getSetting(DefaultSettings.HTTPS_HOST) : cfg.getSetting(DefaultSettings.HTTP_HOST);
			host = hostname + ":" + port;
		}

		return HttpURLPath.of((c.isConnectionSecure() ? "https" : "http") + "://" + host + "/auth/" + getID() + "/response"); // TODO: verify host
	}

	public default HttpURLPath getPostAuthRedirectURL() {
		return HttpURLPath.of(HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("from", "/"));
	}

	public default boolean getShouldConnect() {
		URLEncoded query = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery();
		return query.has("connect") && query.getFirst("connect", "/").equals("true");
	}

	public default File getConfigurationDirectory() {
		return new File(Webinterface.getConfigurationDirectory(), "auth/" + getID() + "/");
	}

	public default boolean isAvailable() {
		return true;
	}

}
