package me.mrletsplay.webinterfaceapi.session;

import static me.mrletsplay.webinterfaceapi.sql.SQLHelper.tableName;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.sql.SQLHelper;

public class SQLSessionStorage implements SessionStorage {

	@Override
	public void initialize() {
		Webinterface.getLogger().debug("Creating tables for session storage");

		SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName("sessions") + "(SessionId VARCHAR(255) PRIMARY KEY, AccountId VARCHAR(255) NOT NULL, ExpiresAt BIGINT NOT NULL, Properties LONGTEXT)")) {
				st.execute();
			}
		});
	}

	@Override
	public void storeSession(Session session) {
		SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("INSERT INTO " + tableName("sessions") + "(SessionId, AccountId, ExpiresAt, Properties) VALUES(?, ?, ?, ?)")) {
				st.setString(1, session.getSessionID());
				st.setString(2, session.getAccountID());
				st.setLong(3, session.getExpiresAt().toEpochMilli());
				st.setString(4, new JSONObject(session.getProperties()).toString());
				st.execute();
			}
		});
	}

	@Override
	public void deleteSession(String sessionID) {
		SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + tableName("sessions") + " WHERE SessionId = ?")) {
				st.setString(1, sessionID);
				st.execute();
			}
		});
	}

	@Override
	public void deleteSessionsByAccountID(String accountID) {
		SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + tableName("sessions") + " WHERE AccountId = ?")) {
				st.setString(1, accountID);
				st.execute();
			}
		});
	}

	@Override
	public Session getSession(String sessionID) {
		return SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + tableName("sessions") + " WHERE SessionId = ?")) {
				st.setString(1, sessionID);
				try(ResultSet r = st.executeQuery()) {
					if(!r.next()) return null;
					return new Session(r.getString("SessionId"), r.getString("AccountId"), Instant.ofEpochMilli(r.getLong("ExpiresAt")), Complex.castMap(new JSONObject(r.getString("Properties")).toMap(), String.class, String.class).get());
				}
			}
		});
	}

	@Override
	public List<Session> getSessions() {
		return SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + tableName("sessions"))) {
				try(ResultSet r = st.executeQuery()) {
					List<Session> sessions = new ArrayList<>();
					while(r.next()) {
						sessions.add(new Session(r.getString("SessionId"), r.getString("AccountId"), Instant.ofEpochMilli(r.getLong("ExpiresAt")), Complex.castMap(new JSONObject(r.getString("Properties")).toMap(), String.class, String.class).get()));
					}
					return sessions;
				}
			}
		});
	}

}
