package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.util.List;

public interface WebinterfaceAccountStorage {
	
	public void initialize();
	
	public WebinterfaceAccount createAccount();

	public void storeAccount(WebinterfaceAccount account);
	
	public void deleteAccount(WebinterfaceAccount account);
	
	public WebinterfaceAccount getAccountByID(String id);
	
	public WebinterfaceAccount getAccountByPrimaryEmail(String email);
	
	public WebinterfaceAccount getAccountByConnectionSpecificID(String connectionName, String id, boolean caseInsensitive);
	
	public default WebinterfaceAccount getAccountByConnectionSpecificID(String connectionName, String id) {
		return getAccountByConnectionSpecificID(connectionName, id, false);
	}
	
	public List<WebinterfaceAccount> getAccounts();
	
}
