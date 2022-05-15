package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.header.HttpURLPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class NoAuth implements AuthMethod {

	public static final String
		ID = "no_auth";

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String getName() {
		return "No Auth";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		String red = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("from", "/");
		HttpURLPath pth = getAuthResponseUrl();
		pth.getQuery().set("from", red);
		c.getServerHeader().getFields().set("Location", pth.toString());
	}

	@Override
	public AccountConnection handleAuthResponse() throws AuthException {
		return new AccountConnection(getID(), "0", "Anonymous", null, null, true);
	}

	@Override
	public boolean isAvailable() {
		return Webinterface.getConfig().getSetting(DefaultSettings.ALLOW_ANONYMOUS);
	}

}
