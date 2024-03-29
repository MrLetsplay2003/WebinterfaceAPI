package me.mrletsplay.webinterfaceapi.session;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;

public class FileSessionStorage implements SessionStorage {

	private File file;
	private FileCustomConfig config;

	public FileSessionStorage(File file) {
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
	public void storeSession(Session session) {
		config.set(session.getSessionID() + ".expires-at", session.getExpiresAt().toEpochMilli());
		config.set(session.getSessionID() + ".account-id", session.getAccountID());
		config.set(session.getSessionID() + ".properties", new JSONObject(session.getProperties()).toString());
		config.saveToFile();
	}

	@Override
	public void deleteSession(String sessionID) {
		config.unset(sessionID);
		config.saveToFile();
	}

	@Override
	public void deleteSessionsByAccountID(String accountID) {
		for(String sessID : config.getKeys()) {
			if(config.getString(sessID + ".account-id").equals(accountID)) deleteSession(sessID);
		}
		config.saveToFile();
	}

	@Override
	public Session getSession(String sessionID) {
		if(!config.isSet(sessionID)) return null;
		return new Session(
				sessionID,
				config.getString(sessionID + ".account-id"),
				Instant.ofEpochMilli(config.getLong(sessionID + ".expires-at")),
				Complex.castMap(new JSONObject(config.getString(sessionID + ".properties")).toMap(), String.class, String.class).get()
			);
	}

	@Override
	public List<Session> getSessions() {
		List<Session> ss = new ArrayList<>();
		for(String sessID : config.getKeys()) {
			ss.add(getSession(sessID));
		}
		return ss;
	}

}
