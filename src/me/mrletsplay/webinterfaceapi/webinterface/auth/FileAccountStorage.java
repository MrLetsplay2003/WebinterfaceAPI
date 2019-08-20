package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;

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
	public WebinterfaceAccount createAccount(String email) {
		WebinterfaceAccount acc = new WebinterfaceAccount(UUID.randomUUID().toString(), email, new ArrayList<>());
		storeAccount(acc);
		return acc;
	}

	@Override
	public void storeAccount(WebinterfaceAccount account) {
		JSONArray arr = new JSONArray();
		for(WebinterfaceAccountData d : account.getData()) {
			JSONObject accD = new JSONObject();
			accD.set("method", d.getAuthMethod());
			accD.set("id", d.getUserID());
			accD.set("name", d.getUserName());
			accD.set("email", d.getUserEmail());
			accD.set("avatar", d.getUserAvatar());
			arr.add(accD);
		}
		config.set(account.getID() + ".email", account.getEmail());
		config.set(account.getID() + ".data", arr);
		config.saveToFile();
	}

	@Override
	public WebinterfaceAccount getAccountByID(String id) {
		if(!config.isSet(id)) return null;
		List<WebinterfaceAccountData> data = new ArrayList<>();
		for(ConfigSection s : config.getComplex(id + ".data", Complex.list(ConfigSection.class), new ArrayList<>(), false)) {
			data.add(new WebinterfaceAccountData(
					s.getString("method"),
					s.getString("id"),
					s.getString("name"),
					s.getString("email"),
					s.getString("avatar"))
				);
		}
		return new WebinterfaceAccount(id, config.getString(id + ".email"), data);
	}

	@Override
	public WebinterfaceAccount getAccountByEmail(String email) {
		for(String id : config.getKeys()) {
			WebinterfaceAccount acc = getAccountByID(id);
			if(acc.getEmail().equals(email)) return acc;
		}
		return null;
	}

}
