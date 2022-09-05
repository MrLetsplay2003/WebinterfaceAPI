package me.mrletsplay.webinterfaceapi.auth;

import java.io.File;

import me.mrletsplay.simplehttpserver.http.header.HttpURLPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public interface AuthMethod {

	public String getID();

	public String getName();

	public void initialize();

	public void handleAuthRequest();

	public AccountConnection handleAuthResponse() throws AuthException;

	public default HttpURLPath getAuthResponseUrl() {
		Config cfg = Webinterface.getConfig();

		String override = cfg.getOverride("auth." + getID() + ".redirect-url", String.class);
		if(override != null) return HttpURLPath.of(override);

		String baseURL = cfg.getSetting(DefaultSettings.HTTP_BASE_URL);
		return HttpURLPath.of(baseURL + "/auth/" + getID() + "/response");
	}

	public default HttpURLPath getRegistrationSecretURL() {
		HttpURLPath path = HttpURLPath.of("/registration-secret");
		path.getQuery().set("from", HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("from", "/"));
		return path;
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

	public default boolean canConnect() {
		return true;
	}

}
