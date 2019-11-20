package me.mrletsplay.webinterfaceapi.webinterface.session;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class WebinterfaceSession {
	
	public static final String COOKIE_NAME = "wi_sessid";
	
	private String sessionID, accountID;
	private Instant expiresAt;
	private Map<String, String> properties;
	
	public WebinterfaceSession(String sessionID, String accountID, Instant expiresAt, Map<String, String> properties) {
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
	
	public WebinterfaceAccount getAccount() {
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
	
	public static WebinterfaceSession startSession(WebinterfaceAccountConnection accountData) {
		String sID = UUID.randomUUID().toString();
		Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
		if(accountData.getUserEmail() == null) {
			throw new AuthException("Login is only supported via Email");
		}
		WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByPrimaryEmail(accountData.getUserEmail());
		if(acc == null) {
			if(!Webinterface.getConfig().getSetting(DefaultSettings.ALLOW_REGISTRATION)) {
				throw new AuthException("Registration not allowed");
			}
			
			acc = Webinterface.getAccountStorage().createAccount();
			acc.addConnection(accountData);
		}
		if(acc.getConnection(accountData.getAuthMethod()) == null) acc.addConnection(accountData);
		WebinterfaceSession s = new WebinterfaceSession(sID, acc.getID(), expiresAt, new HashMap<>());
		Webinterface.getSessionStorage().deleteSessionsByAccountID(acc.getID());
		Webinterface.getSessionStorage().storeSession(s);
		return s;
	}
	
	public static void stopSession() {
		WebinterfaceSession currS = getCurrentSession();
		if(currS == null) return;
		Webinterface.getSessionStorage().deleteSession(currS.getSessionID());
	}
	
	public static WebinterfaceSession getSession(String sessionID) {
		return Webinterface.getSessionStorage().getSession(sessionID);
	}
	
	public static WebinterfaceSession getCurrentSession() {
		String sID = HttpRequestContext.getCurrentContext().getClientHeader().getFields().getCookie(COOKIE_NAME);
		if(sID == null) return null;
		return getSession(sID);
	}

}
