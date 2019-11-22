package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.permission.Permission;

public class FileAccountStorage implements WebinterfaceAccountStorage {
	
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
	public WebinterfaceAccount createAccount() {
		WebinterfaceAccount acc = new WebinterfaceAccount(UUID.randomUUID().toString(), new ArrayList<>(), new ArrayList<>());
		storeAccount(acc);
		return acc;
	}

	@Override
	public void storeAccount(WebinterfaceAccount account) {
		JSONArray arr = new JSONArray();
		for(WebinterfaceAccountConnection c : account.getConnections()) {
			JSONObject accCon = new JSONObject();
			accCon.set("method", c.getAuthMethod());
			accCon.set("id", c.getUserID());
			accCon.set("name", c.getUserName());
			accCon.set("email", c.getUserEmail());
			accCon.set("avatar", c.getUserAvatar());
			arr.add(accCon);
		}
		config.set(account.getID() + ".connections", arr);
		config.set(account.getID() + ".permissions", account.getPermissions().stream().map(Permission::getPermission).collect(Collectors.toList()));
		config.saveToFile();
	}

	@Override
	public WebinterfaceAccount getAccountByID(String id) {
		if(!config.isSet(id)) return null;
		List<WebinterfaceAccountConnection> connections = new ArrayList<>();
		for(ConfigSection s : config.getComplex(id + ".connections", Complex.list(ConfigSection.class), new ArrayList<>(), false)) {
			connections.add(new WebinterfaceAccountConnection(
					s.getString("method"),
					s.getString("id"),
					s.getString("name"),
					s.getString("email"),
					s.getString("avatar"))
				);
		}
		List<Permission> perms = config.getStringList(id + ".permissions").stream()
				.map(Permission::new)
				.collect(Collectors.toList());
		return new WebinterfaceAccount(id, connections, perms);
	}

	@Override
	public WebinterfaceAccount getAccountByPrimaryEmail(String email) {
		if(email == null) return null;
		for(String id : config.getKeys()) {
			WebinterfaceAccount acc = getAccountByID(id);
			String pEmail = acc.getPrimaryEmail();
			if(pEmail != null && pEmail.equals(email)) return acc;
		}
		return null;
	}
	
	@Override
	public List<WebinterfaceAccount> getAccounts() {
		return config.getKeys().stream().map(this::getAccountByID).collect(Collectors.toList());
	}

}
