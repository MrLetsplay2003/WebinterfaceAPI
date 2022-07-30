package me.mrletsplay.webinterfaceapi.auth;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.dbcp2.BasicDataSource;

import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.webinterfaceapi.exception.StorageException;


public class MySQLCredentialsStorage implements CredentialsStorage {

	private String url;
	private String username;
	private String password;
	private String database;
	private String tablePrefix;
	private BasicDataSource dataSource;

	private SecureRandom random;

	public MySQLCredentialsStorage(String url, String username, String password, String database, String tablePrefix) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.database = database;
		this.tablePrefix = tablePrefix;
		this.random = new SecureRandom();
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
			try(PreparedStatement st = c.prepareStatement("CREATE TABLE IF NOT EXISTS " + prefix("credentials") + "(`Id` VARCHAR(255) PRIMARY KEY, Salt BLOB NOT NULL, Hash BLOB NOT NULL)")) {
				st.execute();
			}
		});
	}

	@Override
	public void storeCredentials(String id, String plainCredentials) {
		byte[] salt = generateSalt();
		byte[] hash = hash(plainCredentials, salt);
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("INSERT INTO " + prefix("credentials") + "(`Id`, Salt, Hash) VALUES(?, ?, ?)")) {
				st.setString(1, id);
				st.setBytes(2, salt);
				st.setBytes(3, hash);
				st.execute();
			}
		});
	}

	@Override
	public void deleteCredentials(String id) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + prefix("credentials") + " WHERE `Id` = ?")) {
				st.setString(1, id);
				st.execute();
			}
		});
	}

	@Override
	public boolean checkCredentials(String id, String plainCredentials) {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + prefix("credentials") + " WHERE `Id` = ?")) {
				st.setString(1, id);
				try(ResultSet r = st.executeQuery()) {
					if(!r.next()) return false;
					byte[] salt = r.getBytes("Salt");
					byte[] hash = r.getBytes("Hash");
					return Arrays.equals(hash, hash(plainCredentials, salt));
				}
			}
		});
	}

	private byte[] generateSalt() {
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	private byte[] hash(String raw, byte[] salt) {
		try {
			KeySpec spec = new PBEKeySpec(raw.toCharArray(), salt, 65536, 128);
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = f.generateSecret(spec).getEncoded();
			return hash;
		}catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new FriendlyException(e);
		}
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
