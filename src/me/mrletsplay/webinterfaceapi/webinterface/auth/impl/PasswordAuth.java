package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.header.HttpClientContentTypes;
import me.mrletsplay.webinterfaceapi.http.header.HttpURLPath;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class PasswordAuth implements WebinterfaceAuthMethod {

	@Override
	public String getID() {
		return "password";
	}

	@Override
	public String getName() {
		return "Password";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		
		HttpURLPath clientPath = HttpRequestContext.getCurrentContext().getClientHeader().getPath();
		String red = clientPath.getQueryParameterValue("from", "/");
		boolean con = clientPath.hasQueryParameter("connect") && clientPath.getQueryParameterValue("connect").equals("true");
		
		HttpURLPath pth = new HttpURLPath("/auth/password/login");
		pth.setQueryParameterValue("from", red);
		if(con) pth.setQueryParameterValue("connect", "true");
		c.getServerHeader().getFields().setFieldValue("Location", pth.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		try {
			Map<String, List<String>> params = (Map<String, List<String>>) c.getClientHeader().getPostData().getParsedAs(HttpClientContentTypes.URLENCODED);
			String username = params.get("username").get(0).toLowerCase(); // NONBETA: allow case
			String password = params.get("password").get(0);
			boolean register = (params.containsKey("register") ? params.get("register").get(0).equalsIgnoreCase("on") : false);
			WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID("password", username);
			if(!register) {
				if(acc == null) throw new AuthException("Invalid username");
				WebinterfaceAccountConnection con = acc.getConnection("password");
				if(!Webinterface.getCredentialsStorage().checkCredentials(username, password)) throw new AuthException("Invalid password");
				return con;
			}else {
				if(acc != null) throw new AuthException("An account with that username already exists");
				Webinterface.getCredentialsStorage().storeCredentials(username, password);
				return new WebinterfaceAccountConnection("password", username, username, null, null);
			}
		}catch(AuthException e) {
			c.getServerHeader().setStatusCode(HttpStatusCodes.FOUND_302);
			c.getServerHeader().getFields().setFieldValue("Location", "/auth/password/login?" + Base64.getEncoder().encodeToString(e.getMessage().getBytes(StandardCharsets.UTF_8)));
			throw e;
		}
	}

}
