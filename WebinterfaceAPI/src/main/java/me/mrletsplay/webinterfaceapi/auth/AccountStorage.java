package me.mrletsplay.webinterfaceapi.auth;

import java.util.List;

public interface AccountStorage {

	public void initialize();

	public Account createAccount(AccountConnection connection);

	public void updateAccount(Account account);

	public void deleteAccount(String accountID);

	public List<AccountConnection> getConnections(String accountID);

	public AccountConnection getConnection(String accountID, String connectionName);

	public void addConnection(String accountID, AccountConnection connection);

	public void removeConnection(String accountID, String connectionName);

	public List<String> getPermissions(String accountID);

	public void setPermissions(String accountID, List<String> permissions);

	public void addPermission(String accountID, String permission);

	public void removePermission(String accountID, String permission);

	public Account getAccountByID(String id);

	public Account getAccountByEmail(String email);

	public Account getAccountByConnectionSpecificID(String connectionName, String id, boolean caseInsensitive);

	public default Account getAccountByConnectionSpecificID(String connectionName, String id) {
		return getAccountByConnectionSpecificID(connectionName, id, false);
	}

	public List<Account> getAccounts();

}
