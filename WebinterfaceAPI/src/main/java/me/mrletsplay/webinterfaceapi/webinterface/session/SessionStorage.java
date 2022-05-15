package me.mrletsplay.webinterfaceapi.webinterface.session;

import java.util.List;

public interface SessionStorage {
	
	public void initialize();
	
	public void storeSession(Session session);
	
	public void deleteSession(String sessionID);
	
	public void deleteSessionsByAccountID(String accountID);
	
	public Session getSession(String sessionID);
	
	public List<Session> getSessions();

}
