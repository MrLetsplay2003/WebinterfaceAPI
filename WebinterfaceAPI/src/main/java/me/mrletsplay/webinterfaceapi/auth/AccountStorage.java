package me.mrletsplay.webinterfaceapi.auth;

import java.util.List;

public interface AccountStorage {
	
	public void initialize();
	
	public Account createAccount();

	public void storeAccount(Account account);
	
	public void deleteAccount(Account account);
	
	public Account getAccountByID(String id);
	
	public Account getAccountByPrimaryEmail(String email);
	
	public Account getAccountByConnectionSpecificID(String connectionName, String id, boolean caseInsensitive);
	
	public default Account getAccountByConnectionSpecificID(String connectionName, String id) {
		return getAccountByConnectionSpecificID(connectionName, id, false);
	}
	
	public List<Account> getAccounts();
	
}
