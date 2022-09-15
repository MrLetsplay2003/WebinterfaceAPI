package me.mrletsplay.webinterfaceapi.auth;

import static me.mrletsplay.webinterfaceapi.sql.SQLHelper.tableName;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.sql.SQLHelper;

public class SQLCredentialsStorage implements CredentialsStorage {

	private SecureRandom random;

	public SQLCredentialsStorage() {
		this.random = new SecureRandom();
	}

	@Override
	public void initialize() {
		Webinterface.getLogger().debug("Creating tables for credentials storage");

		SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName("credentials") + "(AuthMethod VARCHAR(255), `Id` VARCHAR(255), Salt BLOB NOT NULL, Hash BLOB NOT NULL, PRIMARY KEY (AuthMethod, `Id`))")) {
				st.execute();
			}
		});
	}

	@Override
	public void storeCredentials(String authMethod, String id, String plainCredentials) {
		byte[] salt = generateSalt();
		byte[] hash = hash(plainCredentials, salt);
		SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("INSERT INTO " + tableName("credentials") + "(AuthMethod, `Id`, Salt, Hash) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE Salt = VALUES(Salt), Hash = VALUES(Hash)")) {
				st.setString(1, authMethod);
				st.setString(2, id);
				st.setBytes(3, salt);
				st.setBytes(4, hash);
				st.execute();
			}
		});
	}

	@Override
	public void deleteCredentials(String authMethod, String id) {
		SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + tableName("credentials") + " WHERE AuthMethod = ? AND `Id` = ?")) {
				st.setString(1, authMethod);
				st.setString(2, id);
				st.execute();
			}
		});
	}

	@Override
	public boolean checkCredentials(String authMethod, String id, String plainCredentials) {
		return SQLHelper.run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + tableName("credentials") + " WHERE AuthMethod = ? AND `Id` = ?")) {
				st.setString(1, authMethod);
				st.setString(2, id);
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

}
