package me.mrletsplay.webinterfaceapi.webinterface.session;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.Account;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class Session {

	public static final String COOKIE_NAME = "wi_sessid";

	public static final Supplier<String> LOGIN_URL = () -> "/login?from=" + HttpUtils.urlEncode(HttpRequestContext.getCurrentContext().getClientHeader().getPath().toString());

	private String
		sessionID,
		accountID;

	private Instant expiresAt;

	private Map<String, String> properties;

	public Session(String sessionID, String accountID, Instant expiresAt, Map<String, String> properties) {
		this.sessionID = sessionID;
		this.accountID = accountID;
		this.expiresAt = expiresAt;
		this.properties = properties;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getAccountID() {
		return accountID;
	}

	public Account getAccount() {
		return Webinterface.getAccountStorage().getAccountByID(accountID);
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public boolean hasExpired() {
		return Instant.now().isAfter(getExpiresAt());
	}

	public void setProperty(String name, String value) {
		properties.put(name, value);
	}

	public String getProperty(String name) {
		return properties.get(name);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public static Session startSession(AccountConnection accountData) {
		String sID = UUID.randomUUID().toString();
		Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

		Account acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(accountData.getConnectionName(), accountData.getUserID());

//		if(acc == null && accountData.getUserEmail() != null) { FIXME
//			acc = Webinterface.getAccountStorage().getAccountByPrimaryEmail(accountData.getUserEmail());
//		}

		if(acc == null) {
			if(!Webinterface.getConfig().getSetting(DefaultSettings.ALLOW_REGISTRATION)) {
				throw new AuthException("Registration not allowed");
			}

			acc = Webinterface.getAccountStorage().createAccount();
			acc.addConnection(accountData);
		}
		if(acc.getConnection(accountData.getConnectionName()) == null) acc.addConnection(accountData);
		Session s = new Session(sID, acc.getID(), expiresAt, new HashMap<>());
		Webinterface.getSessionStorage().storeSession(s);
		return s;
	}

	public static void stopSession() {
		Session currS = getCurrentSession();
		if(currS == null) return;
		Webinterface.getSessionStorage().deleteSession(currS.getSessionID());
	}

	public static Session getSession(String sessionID) {
		return Webinterface.getSessionStorage().getSession(sessionID);
	}

	public static Session getCurrentSession() {
		String sID = HttpRequestContext.getCurrentContext().getClientHeader().getFields().getCookie(COOKIE_NAME);
		if(sID == null) return null;
		return getSession(sID);
	}

	/**
	 * Checks whether there is currently a session active and redirects the user to the login page if not.<br>
	 * Can only be called inside HTTP contexts (e.g. {@link HttpDocument#createContent()}).<br>
	 * This method should be called as the first method (before creating any content).<br>
	 * The calling method should not modify the HTTP server header afterwards if this method returns <code>false</code> and instead just return.<br>
	 * <br>
	 * Example code:<br>
	 * <code>
	 * public void createContent() {<br>
	 * &nbsp;&nbsp;if(!WebinterfaceSession.requireSession()) return;<br>
	 * <br>
	 * &nbsp;&nbsp;// ... Create the content of the page ...<br>
	 * }
	 * </code>
	 * @return <code>true</code> if the current user is logged in, <code>false</code> otherwise
	 */
	public static boolean requireSession() {
		if(Session.getCurrentSession() == null || Session.getCurrentSession().getAccount() == null) {
			HttpRequestContext c = HttpRequestContext.getCurrentContext();
			c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			c.getServerHeader().getFields().set("Location", LOGIN_URL.get());
			return false;
		}

		return true;
	}

}
