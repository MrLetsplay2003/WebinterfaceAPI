package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.header.HttpURLPath;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class NoAuth implements WebinterfaceAuthMethod {
	
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
		String red = HttpRequestContext.getCurrentContext().getClientHeader().getPath().getQueryParameterValue("from", "/");
		HttpURLPath pth = getAuthResponseUrl();
		pth.setQueryParameterValue("from", red);
		c.getServerHeader().getFields().setFieldValue("Location", pth.toString());
	}

	@Override
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException {
		return new WebinterfaceAccountConnection(getID(), "0", "Anonymous", null, null, true);
	}

}
