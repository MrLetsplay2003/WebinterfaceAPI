package me.mrletsplay.webinterfaceapi.auth;

import java.io.File;

import me.mrletsplay.simplehttpserver.http.HttpRequestMethod;
import me.mrletsplay.simplehttpserver.http.header.HttpUrlPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.UrlEncoded;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public interface AuthMethod {

	public String getID();

	public String getName();

	public void initialize();

	public void handleAuthRequest();

	public default HttpRequestMethod getAuthResponseMethod() {
		return HttpRequestMethod.GET;
	}

	public AccountConnection handleAuthResponse() throws AuthException;

	public default HttpUrlPath getAuthResponseUrl() {
		Config cfg = Webinterface.getConfig();

		String override = cfg.getOverride("auth." + getID() + ".redirect-url", String.class);
		if(override != null) return HttpUrlPath.of(override);

		String baseURL = cfg.getSetting(DefaultSettings.HTTP_BASE_URL);
		return HttpUrlPath.of(baseURL + "/auth/" + getID() + "/response");
	}

	public default HttpUrlPath getRegistrationSecretURL() {
		HttpUrlPath path = HttpUrlPath.of("/registration-secret");
		path.getQuery().set("from", HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("from", "/"));
		return path;
	}

	public default HttpUrlPath getPostAuthRedirectURL() {
		return HttpUrlPath.of(HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("from", "/"));
	}

	public default boolean getShouldConnect() {
		UrlEncoded query = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery();
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
