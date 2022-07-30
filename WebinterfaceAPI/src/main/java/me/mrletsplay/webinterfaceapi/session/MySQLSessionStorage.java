package me.mrletsplay.webinterfaceapi.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.exception.StorageException;


public class MySQLSessionStorage implements SessionStorage {

	private String url;
	private String username;
	private String password;
	private String database;
	private String tablePrefix;
	private BasicDataSource dataSource;

	public MySQLSessionStorage(String url, String username, String password, String database, String tablePrefix) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.database = database;
		this.tablePrefix = tablePrefix;
	}

	private String prefix(String tableName) {
		if(tablePrefix == null || tablePrefix.isBlank()) {
			return tableName;
		}

		return tablePrefix + "_" + tableName;
	}

	private void run(SQLAction action) {
		try(Connection c = dataSource.getConnection()) {
			action.run(c);
		}catch(SQLException e) {
			throw new StorageException("MySQL error", e);
		}
	}

	private <T> T run(ReturningSQLAction<T> action) {
		try(Connection c = dataSource.getConnection()) {
			return action.run(c);
		}catch(SQLException e) {
			throw new StorageException("MySQL error", e);
		}
	}

	@Override
	public void initialize() {
		this.dataSource = new BasicDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaxTotal(10);
		dataSource.setDefaultQueryTimeout(10);
		dataSource.setMaxWaitMillis(30000);
		dataSource.setDefaultSchema(database);
		dataSource.setDefaultCatalog(database);

		run(c -> {
			try(PreparedStatement st = c.prepareStatement("CREATE TABLE IF NOT EXISTS " + prefix("sessions") + "(SessionId VARCHAR(255) PRIMARY KEY, AccountId VARCHAR(255) NOT NULL, ExpiresAt BIGINT NOT NULL, Properties LONGTEXT)")) {
				st.execute();
			}
		});
	}

	@Override
	public void storeSession(Session session) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("INSERT INTO " + prefix("sessions") + "(SessionId, AccountId, ExpiresAt, Properties) VALUES(?, ?, ?, ?)")) {
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
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + prefix("sessions") + " WHERE SessionId = ?")) {
				st.setString(1, sessionID);
				st.execute();
			}
		});
	}

	@Override
	public void deleteSessionsByAccountID(String accountID) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + prefix("sessions") + " WHERE AccountId = ?")) {
				st.setString(1, accountID);
				st.execute();
			}
		});
	}

	@Override
	public Session getSession(String sessionID) {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + prefix("sessions") + " WHERE SessionId = ?")) {
				st.setString(1, sessionID);
				try(ResultSet r = st.executeQuery()) {
					if(!r.next()) return null;
					return new Session(r.getString("SessionId"), r.getString("AccountId"), Instant.ofEpochMilli(r.getLong("ExpiresAt")), Complex.castMap(new JSONObject(r.getString("Properties")), String.class, String.class).get());
				}
			}
		});
	}

	@Override
	public List<Session> getSessions() {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + prefix("sessions"))) {
				try(ResultSet r = st.executeQuery()) {
					List<Session> sessions = new ArrayList<>();
					while(r.next()) {
						sessions.add(new Session(r.getString("SessionId"), r.getString("AccountId"), Instant.ofEpochMilli(r.getLong("ExpiresAt")), Complex.castMap(new JSONObject(r.getString("Properties")), String.class, String.class).get()));
					}
					return sessions;
				}
			}
		});
	}

	@FunctionalInterface
	private interface SQLAction {

		public void run(Connection connection) throws SQLException;

	}

	@FunctionalInterface
	private interface ReturningSQLAction<T> {

		public T run(Connection connection) throws SQLException;

	}

}
