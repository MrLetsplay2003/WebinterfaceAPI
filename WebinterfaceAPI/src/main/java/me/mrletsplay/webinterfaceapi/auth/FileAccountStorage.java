package me.mrletsplay.webinterfaceapi.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.permission.Permission;

public class FileAccountStorage implements AccountStorage {

	private File file;
	private FileCustomConfig config;

	public FileAccountStorage(File file) {
		this.file = file;
	}

	@Override
	public void initialize() {
		this.config = ConfigLoader.loadFileConfig(file);
	}

	public File getFile() {
		return file;
	}

	@Override
	public Account createAccount(AccountConnection connection) {
		Account acc = new Account(UUID.randomUUID().toString(), connection.getUserName());
		updateAccount(acc);
		acc.addConnection(connection);
		return acc;
	}

	@Override
	public void updateAccount(Account account) {
		config.set(account.getID() + ".username", account.getUsername());
		config.set(account.getID() + ".avatar", account.getAvatar());
		config.set(account.getID() + ".email", account.getEmail());
		config.set(account.getID() + ".permissions", account.getPermissions().stream().map(Permission::getPermission).collect(Collectors.toList()));
		config.saveToFile();
	}

	@Override
	public void deleteAccount(String accountID) {
		config.unset(accountID);
		config.saveToFile();
	}

	@Override
	public Account getAccountByID(String id) {
		if(!config.isSet(id)) return null;
		return new Account(id, config.getString(id + ".username"), config.getString(id + ".avatar"), config.getString(id + ".email"));
	}

	@Override
	public Account getAccountByEmail(String email) {
		if(email == null) return null;
		for(String id : config.getKeys()) {
			Account acc = getAccountByID(id);
			if(acc == null) continue;
			String pEmail = acc.getEmail();
			if(pEmail != null && pEmail.equals(email)) return acc;
		}
		return null;
	}

	@Override
	public Account getAccountByConnectionSpecificID(String connectionName, String id, boolean caseInsensitive) {
		if(id == null) return null;
		for(String aID : config.getKeys()) {
			AccountConnection con = getConnection(aID, connectionName);
			if(con == null) continue;
			String tID = con.getUserID();
			if(tID != null && (caseInsensitive ? tID.equalsIgnoreCase(id) : tID.equals(id))) return getAccountByID(aID);
		}
		return null;
	}

	@Override
	public List<Account> getAccounts() {
		return config.getKeys().stream()
			.map(this::getAccountByID)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	@Override
	public List<AccountConnection> getConnections(String accountID) {
		List<AccountConnection> connections = new ArrayList<>();
//		System.out.println(config.toJSON().toFancyString());
		config.saveToFile();
		for(String conName : config.getKeys(accountID + ".connections")) {
			AccountConnection con = new AccountConnection(
				conName,
				config.getString(accountID + ".connections." + conName + ".id"),
				config.getString(accountID + ".connections." + conName + ".name"),
				config.getString(accountID + ".connections." + conName + ".email"),
				config.getString(accountID + ".connections." + conName + ".avatar")
			);
			connections.add(con);
		}
		return connections;
	}

	@Override
	public AccountConnection getConnection(String accountID, String connectionName) {
		return new AccountConnection(
			connectionName,
			config.getString(accountID + ".connections." + connectionName + ".id"),
			config.getString(accountID + ".connections." + connectionName + ".name"),
			config.getString(accountID + ".connections." + connectionName + ".email"),
			config.getString(accountID + ".connections." + connectionName + ".avatar")
		);
	}

	@Override
	public void addConnection(String accountID, AccountConnection connection) {
		System.out.println(accountID + "/" + connection.getConnectionName());
		if(config.isSet(accountID + ".connections." + connection.getConnectionName())) return; // Connection already exists
		config.set(accountID + ".connections." + connection.getConnectionName() + ".id", connection.getUserID());
		config.set(accountID + ".connections." + connection.getConnectionName() + ".name", connection.getUserName());
		config.set(accountID + ".connections." + connection.getConnectionName() + ".email", connection.getUserEmail());
		config.set(accountID + ".connections." + connection.getConnectionName() + ".avatar", connection.getUserAvatar());
		config.saveToFile();
	}

	@Override
	public void removeConnection(String accountID, String connectionName) {
		config.unset(accountID + ".connections." + connectionName);
		config.saveToFile();
	}

	@Override
	public List<String> getPermissions(String accountID) {
		return config.getStringList(accountID + ".permissions").stream()
			.distinct()
			.collect(Collectors.toList());
	}

	@Override
	public void setPermissions(String accountID, List<String> permissions) {
		config.set(accountID + ".permissions", permissions);
		config.saveToFile();
	}

	@Override
	public void addPermission(String accountID, String permission) {
		List<String> perms = getPermissions(accountID);
		perms.add(permission);
		setPermissions(accountID, perms);
	}

	@Override
	public void removePermission(String accountID, String permission) {
		List<String> perms = getPermissions(accountID);
		perms.remove(permission);
		setPermissions(accountID, perms);
	}

}
