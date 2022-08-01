package me.mrletsplay.webinterfaceapi.auth;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class FileCredentialsStorage implements CredentialsStorage {

	private File file;
	private FileCustomConfig config;

	private SecureRandom random;

	public FileCredentialsStorage(File file) {
		this.file = file;
		this.random = new SecureRandom();
	}

	@Override
	public void initialize() {
		this.config = ConfigLoader.loadFileConfig(file);
	}

	@Override
	public void storeCredentials(String authMethod, String id, String plainCredentials) {
		byte[] salt = generateSalt();
		config.set(authMethod + "." + id + ".salt", Base64.getEncoder().encodeToString(salt));
		config.set(authMethod + "." + id + ".hash", hash(plainCredentials, salt));
		config.saveToFile();
	}

	@Override
	public void deleteCredentials(String authMethod, String id) {
		config.unset(authMethod + "." + id);
	}

	@Override
	public boolean checkCredentials(String authMethod, String id, String plainCredentials) {
		if(!config.isSet(id)) return false;
		byte[] salt = Base64.getDecoder().decode(config.getString(authMethod + "." + id + ".salt"));
		String hash = config.getString(authMethod + "." + id + ".hash");
		return hash(plainCredentials, salt).equals(hash);
	}

	private byte[] generateSalt() {
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	private String hash(String raw, byte[] salt) {
		try {
			KeySpec spec = new PBEKeySpec(raw.toCharArray(), salt, 65536, 128);
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = f.generateSecret(spec).getEncoded();
			return Base64.getEncoder().encodeToString(hash);
		}catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new FriendlyException(e);
		}
	}

}
