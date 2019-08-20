package me.mrletsplay.webinterfaceapi.webinterface.session;

public interface WebinterfaceSessionStorage {
	
	public void initialize();
	
	public void storeSession(WebinterfaceSession session);
	
	public void deleteSession(String sessionID);
	
	public void deleteSessionsByAccountID(String accountID);
	
	public WebinterfaceSession getSession(String sessionID);

}
