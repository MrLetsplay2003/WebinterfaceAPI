package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class NoAuth implements WebinterfaceAuthMethod {

	@Override
	public String getID() {
		return "no_auth";
	}

	@Override
	public String getName() {
		return "No Auth";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.MOVED_PERMANENTLY_301);
		c.getServerHeader().getFields().setFieldValue("Location", getAuthResponseUrl());
	}

	@Override
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException {
		return new WebinterfaceAccountConnection("no_auth", "0", "Anonymous", null, null, true);
	}

}
