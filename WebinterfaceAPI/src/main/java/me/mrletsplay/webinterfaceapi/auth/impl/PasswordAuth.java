package me.mrletsplay.webinterfaceapi.auth.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

import me.mrletsplay.simplehttpserver.http.HttpRequestMethod;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.header.DefaultClientContentTypes;
import me.mrletsplay.simplehttpserver.http.header.HttpUrlPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.UrlEncoded;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.auth.AuthException;
import me.mrletsplay.webinterfaceapi.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class PasswordAuth implements AuthMethod {

	public static final String
		ID = "password";

	private static final Pattern VALID_USERNAME = Pattern.compile("[a-zA-Z0-9-_.]{2,64}"); // 2 - 64 chars, alphanumeric with -_.

	@Override
	public void initialize() {
		// Nothing to do
	}

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

		UrlEncoded query = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery();
		String red = query.getFirst("from", "/");
		boolean con = query.has("connect") && query.getFirst("connect").equals("true");

		HttpUrlPath pth = new HttpUrlPath("/auth/password/login");
		pth.getQuery().set("from", red);
		if(con) pth.getQuery().set("connect", "true");
		c.getServerHeader().getFields().set("Location", pth.toString());
	}

	@Override
	public HttpRequestMethod getAuthResponseMethod() {
		return HttpRequestMethod.POST;
	}

	@Override
	public AccountConnection handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		try {
			UrlEncoded params = c.getClientHeader().getPostData().getParsedAs(DefaultClientContentTypes.URLENCODED);
			String username = params.getFirst("username"); // NONBETA: allow case
			String password = params.getFirst("password");
			boolean register = (params.has("register") ? params.getFirst("register").equalsIgnoreCase("on") : false);
			Account acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(ID, username, true);
			if(!register) {
				if(acc == null) throw new AuthException("Invalid username/password");
				AccountConnection con = acc.getConnection(ID);
				if(!Webinterface.getCredentialsStorage().checkCredentials(ID, username, password)) throw new AuthException("Invalid username/password");
				return con;
			}else {
				if(!isValidUsername(username)) throw new AuthException("Username contains invalid characters or is too long/short");
				if(acc != null) throw new AuthException("An account with that username already exists");
				return new AccountConnection(getID(), username, username, null, null, () -> Webinterface.getCredentialsStorage().storeCredentials(ID, username, password));
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
