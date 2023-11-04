package me.mrletsplay.webinterfaceapi.auth.impl;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.header.HttpUrlPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.auth.AuthException;
import me.mrletsplay.webinterfaceapi.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class NoAuth implements AuthMethod {

	public static final String
		ID = "no_auth";

	private static final String
		ANON_ID = "anonymous";

	// TODO: Make account not deletable
	// TODO: Don't allow connecting other auth methods to the anonymous account
	private static final AccountConnection
		ANON_CONNECTION = new AccountConnection(ID, ANON_ID, "Anonymous User", null, null);

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String getName() {
		return "No Auth";
	}

	@Override
	public void initialize() {
		Account acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(ID, ANON_ID);
		if(acc == null) {
			Webinterface.getLogger().debug("Creating anonymous user for no_auth");
			Webinterface.getAccountStorage().createAccount(ANON_CONNECTION);
		}
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		String red = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("from", "/");
		HttpUrlPath pth = getAuthResponseUrl();
		pth.getQuery().set("from", red);
		c.getServerHeader().getFields().set("Location", pth.toString());
	}

	@Override
	public AccountConnection handleAuthResponse() throws AuthException {
		return new AccountConnection(getID(), ANON_ID, "Anonymous", null, null);
	}

	@Override
	public boolean isAvailable() {
		return Webinterface.getConfig().getSetting(DefaultSettings.ALLOW_ANONYMOUS);
	}

	@Override
	public boolean canConnect() {
		return false;
	}

}
