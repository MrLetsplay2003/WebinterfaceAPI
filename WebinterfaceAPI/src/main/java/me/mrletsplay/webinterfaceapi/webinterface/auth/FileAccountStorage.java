package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.permission.Permission;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

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
	public Account createAccount() {
		Account acc = new Account(UUID.randomUUID().toString(), new ArrayList<>(), new ArrayList<>());
		storeAccount(acc);
		return acc;
	}

	@Override
	public void storeAccount(Account account) {
		JSONArray arr = new JSONArray();
		for(AccountConnection c : account.getConnections()) {
			JSONObject accCon = new JSONObject();
			accCon.set("connection", c.getConnectionName());
			accCon.set("id", c.getUserID());
			accCon.set("name", c.getUserName());
			accCon.set("email", c.getUserEmail());
			accCon.set("avatar", c.getUserAvatar());
			accCon.set("temporary", c.isTemporary());
			arr.add(accCon);
		}
		config.set(account.getID() + ".connections", arr);
		config.set(account.getID() + ".permissions", account.getPermissions().stream().map(Permission::getPermission).collect(Collectors.toList()));
		config.saveToFile();
	}
	
	@Override
	public void deleteAccount(Account account) {
		config.unset(account.getID());
		config.saveToFile();
	}

	@Override
	public Account getAccountByID(String id) {
		if(!config.isSet(id)) return null;
		List<AccountConnection> connections = new ArrayList<>();
		for(ConfigSection s : config.getComplex(id + ".connections", Complex.list(ConfigSection.class), new ArrayList<>(), false)) {
			AccountConnection con = new AccountConnection(
					s.getString("connection"),
					s.getString("id"),
					s.getString("name"),
					s.getString("email"),
					s.getString("avatar"),
					s.getBoolean("temporary")
			);
			if(con.isTemporary() && Webinterface.getSessionStorage().getSessions().stream().noneMatch(sess -> sess.getAccountID().equals(id)))
				continue; // Remove temporary account connections if session is gone
			connections.add(con);
		}
		if(connections.isEmpty()) {
			config.unset(id);
			config.saveToFile();
			return null;
		}
		List<Permission> perms = config.getStringList(id + ".permissions").stream()
				.map(Permission::new)
				.distinct()
				.collect(Collectors.toList());
		return new Account(id, connections, perms);
	}

	@Override
	public Account getAccountByPrimaryEmail(String email) {
		if(email == null) return null;
		for(String id : config.getKeys()) {
			Account acc = getAccountByID(id);
			if(acc == null) continue;
			String pEmail = acc.getPrimaryEmail();
			if(pEmail != null && pEmail.equals(email)) return acc;
		}
		return null;
	}
	
	@Override
	public Account getAccountByConnectionSpecificID(String connectionName, String id, boolean caseInsensitive) {
		if(id == null) return null;
		for(String aID : config.getKeys()) {
			Account acc = getAccountByID(aID);
			if(acc == null) continue;
			AccountConnection con = acc.getConnection(connectionName);
			if(con == null) continue;
			String tID = con.getUserID();
			if(tID != null && (caseInsensitive ? tID.equalsIgnoreCase(id) : tID.equals(id))) return acc;
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

}
