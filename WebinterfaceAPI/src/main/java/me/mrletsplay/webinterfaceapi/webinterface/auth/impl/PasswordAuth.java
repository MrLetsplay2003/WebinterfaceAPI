package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.header.DefaultClientContentTypes;
import me.mrletsplay.simplehttpserver.http.header.HttpURLPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class PasswordAuth implements WebinterfaceAuthMethod {

	public static final String
		ID = "password";

	private static final Pattern VALID_USERNAME = Pattern.compile("[a-zA-Z0-9-_.]{2,64}"); // 2 - 64 chars, alphanumeric with -_.

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String getName() {
		return "Password";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);

		URLEncoded query = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery();
		String red = query.getFirst("from", "/");
		boolean con = query.has("connect") && query.getFirst("connect").equals("true");

		HttpURLPath pth = new HttpURLPath("/auth/password/login");
		pth.getQuery().set("from", red);
		if(con) pth.getQuery().set("connect", "true");
		c.getServerHeader().getFields().set("Location", pth.toString());
	}

	@Override
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		try {
			URLEncoded params = c.getClientHeader().getPostData().getParsedAs(DefaultClientContentTypes.URLENCODED);
			String username = params.getFirst("username"); // NONBETA: allow case
			String password = params.getFirst("password");
			boolean register = (params.has("register") ? params.getFirst("register").equalsIgnoreCase("on") : false);
			WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(ID, username, true);
			if(!register) {
				if(acc == null) throw new AuthException("Invalid username/password");
				WebinterfaceAccountConnection con = acc.getConnection(ID);
				if(!Webinterface.getCredentialsStorage().checkCredentials(username, password)) throw new AuthException("Invalid username/password");
				return con;
			}else {
				if(!Webinterface.getConfig().getSetting(DefaultSettings.ALLOW_REGISTRATION)) throw new AuthException("Registration is disabled");
				if(!isValidUsername(username)) throw new AuthException("Username contains invalid characters or is too long/short");
				if(acc != null) throw new AuthException("An account with that username already exists");
				Webinterface.getCredentialsStorage().storeCredentials(username, password);
				return new WebinterfaceAccountConnection(getID(), username, username, null, null);
			}
		}catch(AuthException e) {
			c.getServerHeader().setStatusCode(HttpStatusCodes.FOUND_302);
			c.getServerHeader().getFields().set("Location", "/auth/password/login?" + Base64.getEncoder().encodeToString(e.getMessage().getBytes(StandardCharsets.UTF_8)));
			throw e;
		}
	}

	public static boolean isValidUsername(String username) {
		return VALID_USERNAME.matcher(username).matches();
	}

	@Override
	public boolean isAvailable() {
		return Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_PASSWORD_AUTH);
	}

}
