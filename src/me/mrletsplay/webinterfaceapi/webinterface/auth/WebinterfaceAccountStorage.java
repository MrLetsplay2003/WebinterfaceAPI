package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.util.List;

public interface WebinterfaceAccountStorage {
	
	public void initialize();
	
	public WebinterfaceAccount createAccount(String email);

	public void storeAccount(WebinterfaceAccount account);
	
	public WebinterfaceAccount getAccountByID(String id);
	
	public WebinterfaceAccount getAccountByEmail(String email);
	
	public List<WebinterfaceAccount> getAccounts();
	
}
